package com.lauchilus.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lauchilus.DTO.AddGameDto;
import com.lauchilus.DTO.AddGameResponse;
import com.lauchilus.DTO.AddPlayedDto;
import com.lauchilus.DTO.AddPlayedResponse;
import com.lauchilus.DTO.AddPlayingDto;
import com.lauchilus.DTO.CollectionDataResponse;
import com.lauchilus.DTO.CreateCollectionDTO;
import com.lauchilus.DTO.GameDTO;
import com.lauchilus.DTO.GameListData;
import com.lauchilus.DTO.ListResponseDto;
import com.lauchilus.DTO.PlayedDataCollection;
import com.lauchilus.DTO.PlayingDataCollection;
import com.lauchilus.DTO.ResponseCollectionDTO;
import com.lauchilus.DTO.ScreenshotsDataGame;
import com.lauchilus.DTO.SearchResponseDto;
import com.lauchilus.DTO.UpdateCollectionDto;
import com.lauchilus.entity.Collection;
import com.lauchilus.entity.Game;
import com.lauchilus.entity.Played;
import com.lauchilus.entity.Playing;
import com.lauchilus.igdb.GameData;
import com.lauchilus.igdb.IgdbService;
import com.lauchilus.repository.CollectionRepository;
import com.lauchilus.repository.GameRepository;
import com.lauchilus.repository.PlayedRepository;
import com.lauchilus.repository.PlayingRepository;
import com.lauchilus.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/gamelist")
@CrossOrigin("**")
public class GameListController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CollectionRepository collectionRepository;

	@Autowired
	private PlayedRepository playedRepository;

	@Autowired
	private PlayingRepository playingRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private IgdbService service;

	@GetMapping
	public String test() {
		return "funciona";
	}

	// post for each endpoint

	// This endpoint creates a collection and receives the following data in the
	// request body: the username as a string, the name of the collection as a
	// string, a description, and an image in byte[].
	@PostMapping("/collection")
	public ResponseEntity createCollection(@RequestBody @Valid CreateCollectionDTO createCollectionDTO,
			UriComponentsBuilder uriComponentsBuilder) {
		Collection collection = collectionRepository
				.save(new Collection(createCollectionDTO, userRepository.findByUsername(createCollectionDTO.user())));
		ResponseCollectionDTO response = new ResponseCollectionDTO(collection.getId(), collection.getName(),
				collection.getDescription(), collection.getImage());
		URI url = uriComponentsBuilder.path("/${id}").buildAndExpand(collection.getId()).toUri();
		return ResponseEntity.created(url).body(response);
	}

	// This endpoint adds a game to a collection. It receives a collection ID as a
	// @PathVariable in the URL and the following data in the request body: an
	// integer game_id. The purpose is to search for a game and then add it to a
	// collection based on its ID.
	@PostMapping("/{collectionid}/game")
	public ResponseEntity addGameToCollection(@PathVariable Integer collectionid,
			@RequestBody @Valid AddGameDto addGamedto) {
		Collection collection = collectionRepository.getReferenceById(collectionid);
		Game game = gameRepository.save(new Game(addGamedto, collection));
		collection.addGame(game);
		gameRepository.save(game);
		collectionRepository.save(collection);
		AddGameResponse response = new AddGameResponse(game);
		return ResponseEntity.ok(response);
	}

	// This endpoint adds a game to the "played" entity. It receives a username as a
	// @PathVariable in the URL, and the request body contains an integer
	// representing the game
	@PostMapping("/{username}/played")
	public ResponseEntity addPlayed(@RequestBody @Valid AddPlayedDto playedDto, @PathVariable String username) {

		Played played = playedRepository.save(new Played(userRepository.findByUsername(username), playedDto));

		AddPlayedResponse response = new AddPlayedResponse(played);
		return ResponseEntity.ok(response);
	}

	// This endpoint adds a game to the "playing" entity. It receives a username as
	// a @PathVariable in the URL, and the request body contains an integer
	// representing the game, a string for the name, a string for the description,
	// and an image in byte[] format.
	@PostMapping("/{username}/playing")
	public ResponseEntity addPlaying(@RequestBody @Valid AddPlayingDto playingDto, @PathVariable String username) {
		Playing playing = playingRepository.save(new Playing(playingDto, userRepository.findByUsername(username)));
		PlayingDataCollection response = new PlayingDataCollection(playing);
		return ResponseEntity.ok(response);
	}

	// GETS

	// This endpoint retrieves all the played games for a given username provided as
	// a @PathVariable in the URL.
	@GetMapping("/{username}/played")
	public ResponseEntity getPlayedList(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable String username) {
		return ResponseEntity
				.ok((playedRepository.findByUsername(username, paginacion).map(PlayedDataCollection::new)));

	}

	// This endpoint retrieves all the playing games for a given username provided
	// as a @PathVariable in the URL.
	@GetMapping("/{username}/playing")
	public ResponseEntity getPlayingList(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable String username) {
		return ResponseEntity
				.ok((playingRepository.findByUsername(username, paginacion).map(PlayingDataCollection::new)));
	}

	//// This endpoint retrieves all the collections for a given username provided
	//// as a @PathVariable in the URL.
	@GetMapping("/{username}/collections")
	public ResponseEntity getCollections(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable String username) {
		return ResponseEntity
				.ok((collectionRepository.findByUsername(username, paginacion).map(ResponseCollectionDTO::new)));
	}

	// This endpoint retrieves all the data of the collection for a given username
	// and collection ID provided as @PathVariable in the URL.
	@GetMapping("/{username}/{collectionid}")
	public ResponseEntity getCollection(@PathVariable String username, @PathVariable Integer collectionid) {
		if (!collectionRepository.existsByUser_idAndId(userRepository.getReferenceByUsername(username).getId(),
				collectionid)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Collection collection = collectionRepository.getReferenceById(collectionid);
		return ResponseEntity.ok(new CollectionDataResponse(collection));
	}

	// This endpoint retrieves a list of all the games in a collection specified as
	// a @PathVariable in the URL.
	@GetMapping("/{collection}/games")
	public ResponseEntity getGamesCollection(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable Integer collection) {
		return ResponseEntity.ok((gameRepository.findByCollection_id(collection, paginacion).map(GameDTO::new)));
	}

	// This endpoint searches for a game in the IGDB API based on a name provided as
	// a @PathVariable in the URL. It retrieves all the available data for the
	// matching game.
	@GetMapping("/{name}")
	public ResponseEntity searchGameByName(@PathVariable String name) throws IOException {
		String response = service.searchGame(name);
		ObjectMapper objectMapper = new ObjectMapper();
		GameData[] dataArray = objectMapper.readValue(response, GameData[].class);
		GameData data = dataArray[0];
		ScreenshotsDataGame[] ss = data.getScreenshots();
		ScreenshotsDataGame screenshotObj = ss[0];
		String imageUrl = ImageBuilderKt.imageBuilder(screenshotObj.getImage_id(), ImageSize.SCREENSHOT_MEDIUM,
				ImageType.PNG);
		String collection = service.getCollection(data.getCollection());
		byte[] image = service.processImage(imageUrl);
		SearchResponseDto resp = new SearchResponseDto(data, image, data.getCollection());
		return ResponseEntity.ok(resp);
	}

	
	@GetMapping("/listGames")
	public ResponseEntity listOfGames() throws IOException {
		String response = service.listGames();
		ObjectMapper objectMapper = new ObjectMapper();
		GameListData[] dataArray = objectMapper.readValue(response, GameListData[].class);

		List<ListResponseDto> responseList = new ArrayList<>();

		for (GameListData data : dataArray) {
			ScreenshotsDataGame[] ss = data.getScreenshots();
			if (ss != null && ss.length > 0) {
				ScreenshotsDataGame screenshotObj = ss[0];
				String imageUrl = ImageBuilderKt.imageBuilder(screenshotObj.getImage_id(), ImageSize.SCREENSHOT_MEDIUM,
						ImageType.PNG);
				byte[] image = service.processImage(imageUrl);
				ListResponseDto resp = new ListResponseDto(data, image);
				responseList.add(resp);
			} else {
				// Manejo si no hay capturas de pantalla
				ListResponseDto resp = new ListResponseDto(data, null);
				responseList.add(resp);
			}
		}
		return ResponseEntity.ok(responseList);
	}

	// This endpoint searches for a game in the IGDB API based on a id provided as a
	// @PathVariable in the URL. It retrieves all the available data for the
	// matching game.
	@GetMapping("/search_id")
	public ResponseEntity searchGameById(@RequestParam Integer id) throws IOException {
		var response = service.searchGameById(id);
		return ResponseEntity.ok(response);
	}

	// UPDATES

	// TODO update entity played and then create endpoint
//	@PutMapping("/{username}/played")
//	public ResponseEntity updatePlayed(@PathVariable String username, @RequestBody UpdatePlayedDto updatePlayedDto) {
//		
//	}

	// This endpoint receives a username and a collection ID as @PathVariable in the
	// URL. The request body contains a name and description as strings, along with
	// an image in byte[] format.
	@PutMapping("/{username}/{collectionid}")
	@Transactional
	public ResponseEntity updateCollection(@PathVariable String username, @PathVariable Integer collectionid,
			@RequestBody @Valid UpdateCollectionDto updateCollection) {
		if (!collectionRepository.existsByUser_idAndId(userRepository.getReferenceByUsername(username).getId(),
				collectionid)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Collection collection = collectionRepository.getReferenceById(collectionid);
		collection.update(updateCollection);
		return ResponseEntity.ok(
				new CollectionDataResponse(collection.getName(), collection.getDescription(), collection.getImage()));
	}

	// This endpoint receives a username and a game playing as @PathVariable in the
	// URL. It finishes the game by moving it from the "playing" entity to the
	// "played" entity. It updates the finish date for the game and removes it from
	// the "playing" entity. Finally, it adds the game to the "played" entity.
	// @PutMapping("/{username}/{playingId}")
	@Transactional
	public ResponseEntity updatePlaying(@PathVariable String username, @PathVariable Integer playingId) {
		if (!playingRepository.existsByUser_idAndId(userRepository.getReferenceByUsername(username).getId(),
				playingId)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Playing playing = playingRepository.getReferenceById(playingId);
		playing.updateFinishDate();
		Played played = playedRepository.save(new Played(playing.getUser(), playing.getGame_id()));
		playingRepository.delete(playing);
		return ResponseEntity.ok("Game finished added to played!");
	}

	// DELETES
	@DeleteMapping("/{username}/playing/{id}")
	public ResponseEntity deletePlaying(@PathVariable String username, @PathVariable Integer id) {
		Playing playing = playingRepository.getReferenceById(id);
		playingRepository.delete(playing);
		return ResponseEntity.ok("Game deleted!");
	}

	@DeleteMapping("/{username}/played/{id}")
	public ResponseEntity deletePlayed(@PathVariable String username, @PathVariable Integer id) {
		Played played = playedRepository.getReferenceById(id);
		playedRepository.delete(played);
		return ResponseEntity.ok("Game deleted!");
	}

	@DeleteMapping("/{username}/{collectionId}")
	public ResponseEntity deleteCollection(@PathVariable String username, @PathVariable Integer collectionId) {
		Collection collection = collectionRepository.getReferenceById(collectionId);
		List<Game> games = gameRepository.findByCollection_id(collectionId);
		gameRepository.deleteAll(games);
		collectionRepository.delete(collection);
		return ResponseEntity.ok("Collection deleted");
	}
}
