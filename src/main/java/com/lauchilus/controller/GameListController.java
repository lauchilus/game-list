package com.lauchilus.controller;

import java.io.IOException;
import java.net.URI;

import org.apache.catalina.connector.Response;
import org.glassfish.jaxb.core.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.lauchilus.DTO.AddGameDto;
import com.lauchilus.DTO.AddGameResponse;
import com.lauchilus.DTO.AddPlayedDto;
import com.lauchilus.DTO.AddPlayedResponse;
import com.lauchilus.DTO.AddPlayingDto;
import com.lauchilus.DTO.CollectionDataResponse;
import com.lauchilus.DTO.CreateCollectionDTO;
import com.lauchilus.DTO.GameDTO;
import com.lauchilus.DTO.PlayedDataCollection;
import com.lauchilus.DTO.PlayingDataCollection;
import com.lauchilus.DTO.ResponseCollectionDTO;
import com.lauchilus.DTO.UpdateCollectionDto;
import com.lauchilus.entity.Collection;
import com.lauchilus.entity.Game;
import com.lauchilus.entity.Played;
import com.lauchilus.entity.Playing;
import com.lauchilus.entity.User;
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

	@PostMapping("/{username}/played")
	public ResponseEntity addPlayed(@RequestBody @Valid AddPlayedDto playedDto, @PathVariable String username) {
//		User user = userRepository.findByUsername(username);
		Played played = playedRepository.save(new Played(userRepository.findByUsername(username), playedDto));

		AddPlayedResponse response = new AddPlayedResponse(played);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{username}/playing")
	public ResponseEntity addPlaying(@RequestBody @Valid AddPlayingDto playingDto, @PathVariable String username) {
		Playing playing = playingRepository.save(new Playing(playingDto, userRepository.findByUsername(username)));
		PlayingDataCollection response = new PlayingDataCollection(playing);
		return ResponseEntity.ok(response);
	}

	// GETS

	@GetMapping("/{username}/played")
	public ResponseEntity getPlayedList(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable String username) {
		return ResponseEntity
				.ok((playedRepository.findByUsername(username, paginacion).map(PlayedDataCollection::new)));

	}

	@GetMapping("/{username}/playing")
	public ResponseEntity getPlayingList(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable String username) {
		return ResponseEntity
				.ok((playingRepository.findByUsername(username, paginacion).map(PlayingDataCollection::new)));
	}

	@GetMapping("/{username}/collections")
	public ResponseEntity getCollections(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable String username) {
		return ResponseEntity
				.ok((collectionRepository.findByUsername(username, paginacion).map(ResponseCollectionDTO::new)));
	}
	
	@GetMapping("/{username}/{collectionid}")
	public ResponseEntity getCollection(@PathVariable String username, @PathVariable Integer collectionid) {
		if(!collectionRepository.existsByUser_idAndId(userRepository.getReferenceByUsername(username).getId(),collectionid)) {
			 return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}		
		Collection collection = collectionRepository.getReferenceById(collectionid);
		return ResponseEntity.ok(new CollectionDataResponse(collection));
	}

	@GetMapping("/{collection}/games")
	public ResponseEntity getGamesCollection(@PageableDefault(size = 10) Pageable paginacion,
			@PathVariable String collection) {
		return ResponseEntity.ok((gameRepository.findByCollection(collection, paginacion).map(GameDTO::new)));
	}

	@GetMapping("/game")
	public ResponseEntity searchGameByName(@RequestParam String name) {
		var response = service.searchGame(name);
		return ResponseEntity.ok(response);
	}

	// search information about a game by id
	@GetMapping("/search_id")
	public ResponseEntity searchGameById(@RequestParam Integer id) throws IOException {
		var response = service.searchGameById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/publisher")
	public ResponseEntity searchcharacter(@RequestParam String character) {
		var response = service.searchCharacter(character);
		return ResponseEntity.ok(response);
	}
	// TODO IMPLEMENT ENDPOINTS FOR UPDATE AND DELETE OPERATIONS

	//UPDATES
	
	// TODO update entity played and then create endpoint
//	@PutMapping("/{username}/played")
//	public ResponseEntity updatePlayed(@PathVariable String username, @RequestBody UpdatePlayedDto updatePlayedDto) {
//		
//	}
	
	@PutMapping("/{username}/{collectionid}")
	@Transactional
	public ResponseEntity updateCollection(@PathVariable String username, @PathVariable Integer collectionid, @RequestBody @Valid UpdateCollectionDto updateCollection) {
		if(!collectionRepository.existsByUser_idAndId(userRepository.getReferenceByUsername(username).getId(),collectionid)) {
			 return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}		
		Collection collection = collectionRepository.getReferenceById(collectionid);
		collection.update(updateCollection);
		return ResponseEntity.ok(new CollectionDataResponse(collection.getName(),collection.getDescription(),collection.getImage()));
	}
	
	@PutMapping("/{username}/{playingId}")
	@Transactional
	public ResponseEntity updatePlaying(@PathVariable String username, @PathVariable Integer playingId) {
		if(!playingRepository.existsByUser_idAndId(userRepository.getReferenceByUsername(username).getId(),playingId)) {
			 return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}		
		Playing playing = playingRepository.getReferenceById(playingId);
		playing.updateFinishDate();
		Played played = playedRepository.save(new Played(playing.getUser(),playing.getGame_id()));
		playingRepository.delete(playing);
		return ResponseEntity.ok("Game finished added to played!");
	}
	
	
	//DELETES
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
	public ResponseEntity deleteCollection(@PathVariable String username,@PathVariable Integer collectionId) {
		Collection collection = collectionRepository.getReferenceById(collectionId);
		collectionRepository.delete(collection);
		return ResponseEntity.ok("Collection deleted");
	}
}
