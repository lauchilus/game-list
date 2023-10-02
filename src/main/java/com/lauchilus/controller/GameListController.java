package com.lauchilus.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.igdb.utils.ImageBuilderKt;
import com.api.igdb.utils.ImageSize;
import com.api.igdb.utils.ImageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lauchilus.DTO.AddGameDto;
import com.lauchilus.DTO.AddGameResponse;
import com.lauchilus.DTO.AddPlayedDto;
import com.lauchilus.DTO.AddPlayingDto;
import com.lauchilus.DTO.CollectionDataResponse;
import com.lauchilus.DTO.CollectionGamesResponsePage;
import com.lauchilus.DTO.CollectionResponsePage;
import com.lauchilus.DTO.CoverGame;
import com.lauchilus.DTO.CreateCollectionDTO;
import com.lauchilus.DTO.GameListData;
import com.lauchilus.DTO.GameMapper;
import com.lauchilus.DTO.GameResponseDTO;
import com.lauchilus.DTO.ListResponseDto;
import com.lauchilus.DTO.PaginationInfo;
import com.lauchilus.DTO.PlayedResponseDto;
import com.lauchilus.DTO.PlayedResponsePage;
import com.lauchilus.DTO.PlayingResponseDto;
import com.lauchilus.DTO.PlayingResponsePage;
import com.lauchilus.DTO.ResponseCollectionDTO;
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
import jakarta.websocket.server.PathParam;

@CrossOrigin(origins="*")
@Controller
@RequestMapping("/gamelist")
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
	
//	@RequestMapping(value = "/*",method = RequestMethod.OPTIONS)
//	public ResponseEntity handle() {
//	    return new ResponseEntity(HttpStatus.OK);
//	}

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
				collection.getDescription(), convertByteArrayToString(collection.getImage()));
		URI url = uriComponentsBuilder.path("/${id}").buildAndExpand(collection.getId()).toUri();
		return ResponseEntity.created(url).body(response);
	}
	
	
	
	
	// This endpoint adds a game to a collection. It receives a collection ID as a
	// @PathVariable in the URL and the following data in the request body: an
	// integer game_id. The purpose is to search for a game and then add it to a
	// collection based on its ID.
	@PostMapping("/{collectionid}/game")
	public ResponseEntity<AddGameResponse> addGameToCollection(@PathVariable Integer collectionid,
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
	public ResponseEntity<String> addPlayed(@RequestBody @Valid AddPlayedDto playedDto, @PathVariable String username) {

		Played played = playedRepository.save(new Played(userRepository.findByUsername(username), playedDto));
//		byte[] ss = service.processImage(generateUrlImage(played.getGame_id().))

//		AddPlayedResponse response = new AddPlayedResponse(played,image);
		return ResponseEntity.ok("ok");
	}

	// This endpoint adds a game to the "playing" entity. It receives a username as
	// a @PathVariable in the URL, and the request body contains an integer
	// representing the game, a string for the name, a string for the description,
	// and an image in byte[] format.
	@PostMapping("/{username}/playing")
	public ResponseEntity addPlaying(@RequestBody @Valid AddPlayingDto playingDto, @PathVariable String username) {
		Playing playing = playingRepository.save(new Playing(playingDto, userRepository.findByUsername(username)));
//		PlayingDataCollection response = new PlayingDataCollection(playing);
		return ResponseEntity.ok().body("OK");
	}

	// GETS

	// This endpoint retrieves all the played games for a given username provided as
	// a @PathVariable in the URL.
	@GetMapping("/{username}/played")
	public ResponseEntity getPlayedList(@PathVariable String username,@PageableDefault(size = 10) Pageable paginacion, @PathParam(value = "0") int page) throws IOException {
		Pageable pageable = PageRequest.of(page, paginacion.getPageSize(), paginacion.getSort());
		Page<Played> pageResult = playedRepository.findByUsername(username,pageable);
		List<Played> playedList = pageResult.getContent();
		ObjectMapper objectMapper = new ObjectMapper();
		List<PlayedResponseDto> responseList = new ArrayList<>();

		for (Played data : playedList) {
			String game = service.searchGameById(data.getGame_id()).toString();
			GameMapper[] dataCover = objectMapper.readValue(game, GameMapper[].class);
			String base64Image = getImageResponse(dataCover[0].getCover());
			PlayedResponseDto response = new PlayedResponseDto(data, dataCover[0].getName(), base64Image,data);
			responseList.add(response);
		}
		System.out.println(responseList.toString());
		PaginationInfo pagination = new PaginationInfo(pageResult.getTotalPages(),pageResult.getTotalElements());
		PlayedResponsePage response = new PlayedResponsePage();
		response.setCollections(responseList);
		response.setPagination(pagination);
		return ResponseEntity.ok(response);

	}

	// This endpoint retrieves all the playing games for a given username provided
	// as a @PathVariable in the URL.
	@GetMapping("/{username}/playing")
	public ResponseEntity<PlayingResponsePage> getPlayingList(@PathVariable String username,@PageableDefault(size = 10) Pageable paginacion, @PathParam(value = "0") int page) throws IOException {
		Pageable pageable = PageRequest.of(page, paginacion.getPageSize(), paginacion.getSort());
		Page<Playing> pageResult = playingRepository.findByUsername(username,pageable);
		List<Playing> playingList = pageResult.getContent();
		ObjectMapper objectMapper = new ObjectMapper();
		List<PlayingResponseDto> responseList = new ArrayList<>();

		for (Playing data : playingList) {
			String game = service.searchGameById(data.getGame_id()).toString();
			GameMapper[] dataCover = objectMapper.readValue(game, GameMapper[].class);
			String base64Image = getImageResponse(dataCover[0].getCover());
			PlayingResponseDto response = new PlayingResponseDto(data,base64Image,dataCover[0].getName());
			responseList.add(response);
		}
		PaginationInfo pagination = new PaginationInfo(pageResult.getTotalPages(),pageResult.getTotalElements());
		PlayingResponsePage response = new PlayingResponsePage();
		response.setCollections(responseList);
		response.setPagination(pagination);
		return ResponseEntity.ok(response);
	}

	//// This endpoint retrieves all the collections for a given username provided
	//// as a @PathVariable in the URL.
	@GetMapping("/{username}/collections")
	public ResponseEntity getCollections(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable String username) {
		Page<Collection> playingList = collectionRepository.findByUsername(username, paginacion);
		List<Collection> pageResult = playingList.getContent();
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<ResponseCollectionDTO> responseDto = new ArrayList<>();

		for (Collection data : playingList) {
			String base64Image = convertByteArrayToString(data.getImage());
			ResponseCollectionDTO response = new ResponseCollectionDTO(data, base64Image);
			responseDto.add(response);
		}
		PaginationInfo pagination = new PaginationInfo(playingList.getTotalPages(),playingList.getTotalElements());
		CollectionResponsePage responseList = new CollectionResponsePage();
		responseList.setCollections(responseDto);
		responseList.setPagination(pagination);
		return ResponseEntity.ok(responseList);
	}

	// This endpoint retrieves all the data of the collection for a given username
	// and collection ID provided as @PathVariable in the URL.
	@GetMapping("/{username}/{collectionid}")
	public ResponseEntity<CollectionDataResponse> getCollection(@PathVariable String username,
			@PathVariable Integer collectionid) {
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
	public ResponseEntity<CollectionGamesResponsePage> getGamesCollection(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable Integer collection, @PathParam(value = "0") int page) throws IOException {
		Pageable pageable = PageRequest.of(page, paginacion.getPageSize(), paginacion.getSort());
		Page<Game> pageResult = gameRepository.findByCollection_id(collection,pageable);
		List<Game> gameList = pageResult.getContent();
		ObjectMapper objectMapper = new ObjectMapper();
		List<GameResponseDTO> responseList = new ArrayList<>();
		for(Game game: gameList) {
			String res = service.searchGameById2(game.getGame_Id());
			GameListData[] data = objectMapper.readValue(res, GameListData[].class);
			GameListData dat = data[0];
			String image = getImageResponse(dat.getCover());
			GameResponseDTO resp = new GameResponseDTO(dat, image,game.getId());
			responseList.add(resp);
		}
		PaginationInfo pagination = new PaginationInfo(pageResult.getTotalPages(),pageResult.getTotalElements());
		CollectionGamesResponsePage response = new CollectionGamesResponsePage();
		response.setCollections(responseList);
		response.setPagination(pagination);
		return ResponseEntity.ok(response);
	}

	// This endpoint searches for a game in the IGDB API based on a name provided as
	// a @PathVariable in the URL. It retrieves all the available data for the
	// matching game.
//	@GetMapping("/{name}")
//	public ResponseEntity<SearchResponseDto> searchGameByName(@PathVariable String name) throws IOException {
//		String response = service.searchGame(name);
//		ObjectMapper objectMapper = new ObjectMapper();
//		GameData[] dataArray = objectMapper.readValue(response, GameData[].class);
//		GameData data = dataArray[0];
//		String image = getImageResponse(data.getCover());
//		SearchResponseDto resp = new SearchResponseDto(data, image, data.getCollection());
//		return ResponseEntity.ok(resp);
//	}

	@GetMapping("/searchByName/{name}")
	public ResponseEntity<List<SearchResponseDto>> searchGamesByName(@PathVariable String name) throws IOException {
		List<SearchResponseDto> results = new ArrayList<>();
		String response = service.searchGame(name);
		ObjectMapper objectMapper = new ObjectMapper();
		GameData[] dataArray = objectMapper.readValue(response, GameData[].class);
		for(GameData data: dataArray) {
			System.out.println(data+"AAAAAAAA");
			if(data.getCover()!=null && data.getSummary()!=null) {
			String image = getImageResponse(data.getCover());
			SearchResponseDto resp = new SearchResponseDto(data, image, data.getCollection());
			results.add(resp);}
		}
//		GameData data = dataArray[0];
//		String image = getImageResponse(data.getCover());
//		SearchResponseDto resp = new SearchResponseDto(data, image, data.getCollection());
		return ResponseEntity.ok(results);
	}
	
	@GetMapping("/listGames")
	public ResponseEntity<List<ListResponseDto>> listOfGames() throws IOException {
		String response = service.listGames();
		ObjectMapper objectMapper = new ObjectMapper();
		GameListData[] dataArray = objectMapper.readValue(response, GameListData[].class);

		List<ListResponseDto> responseList = new ArrayList<>();

		for (GameListData data : dataArray) {
			String image = getImageResponse(data.getCover());
			ListResponseDto resp = new ListResponseDto(data, image);
			responseList.add(resp);

		}
		return ResponseEntity.ok(responseList);
	}

	// This endpoint searches for a game in the IGDB API based on a id provided as a
	// @PathVariable in the URL. It retrieves all the available data for the
	// matching game.
	@CrossOrigin("http://localhost:4200/id/**")
	@GetMapping("/details/{id}")
	public ResponseEntity<SearchResponseDto> searchGameById(@PathVariable Integer id) throws IOException {
		String response = service.searchGameById(id);
		ObjectMapper objectMapper = new ObjectMapper();
		GameData[] dataArray = objectMapper.readValue(response, GameData[].class);
		GameData data = dataArray[0];
		String image = getImageResponse(data.getCover());
		SearchResponseDto resp = new SearchResponseDto(data, image, data.getCollection());
		return ResponseEntity.ok(resp);
	}

	// UPDATES

	// This endpoint receives a username and a collection ID as @PathVariable in the
	// URL. The request body contains a name and description as strings, along with
	// an image in byte[] format.
	@PutMapping("/{username}/{collectionid}")
	@Transactional
	public ResponseEntity<CollectionDataResponse> updateCollection(@PathVariable String username,
			@PathVariable Integer collectionid, @RequestBody @Valid UpdateCollectionDto updateCollection) {
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
	
	@DeleteMapping("/{username}/collection/{gameId}")
	public ResponseEntity deleteGameFromCollection(@PathVariable String username, @PathVariable Integer gameId) {
		Game game = gameRepository.getReferenceById(gameId);
		gameRepository.delete(game);
		return ResponseEntity.ok("game from collection deleted");
	}

	// helpers
	private String getImageResponse(CoverGame data) throws IOException {
		String ss = data.getImage_id();
		String imageUrl = ImageBuilderKt.imageBuilder(ss, ImageSize.SCREENSHOT_BIG, ImageType.PNG);
		byte[] image = service.processImage(imageUrl);
		String base64Image = Base64.getEncoder().encodeToString(image);
		return base64Image;
	}

	private String convertByteArrayToString(byte[] image) {
		String base64Image = Base64.getEncoder().encodeToString(image);
		return base64Image;
	}
}
