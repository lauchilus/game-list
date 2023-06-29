package com.lauchilus.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.lauchilus.DTO.AddPlayedDto;
import com.lauchilus.DTO.AddPlayingDto;
import com.lauchilus.DTO.CreateCollectionDTO;
import com.lauchilus.DTO.ResponseCollectionDTO;
import com.lauchilus.DTO.ResponseUserRegister;
import com.lauchilus.DTO.UserRegisterDTO;
import com.lauchilus.entity.Collection;
import com.lauchilus.entity.Played;
import com.lauchilus.entity.Playing;
import com.lauchilus.entity.User;
import com.lauchilus.igdb.IgdbService;
import com.lauchilus.repository.CollectionRepository;
import com.lauchilus.repository.PlayedRepository;
import com.lauchilus.repository.PlayingRepository;
import com.lauchilus.repository.UserRepository;

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
	private IgdbService service;
	
	@GetMapping
	public String test() {
		return "funciona";
	}
	
	@PostMapping("/register")
	public ResponseEntity<ResponseUserRegister> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO, UriComponentsBuilder uriComponentsBuilder) {
		User user = userRepository.save(new User(userRegisterDTO));
			ResponseUserRegister response = new ResponseUserRegister(user.getId(),user.getUsername());
			URI url = uriComponentsBuilder.path("/{id}").buildAndExpand(user.getId()).toUri();
//			return ResponseEntity.created(url).body(response);
			return ResponseEntity.ok(response);
	}
	
	
	@PostMapping("/collection")
	public ResponseEntity createCollection(@RequestBody @Valid CreateCollectionDTO createCollectionDTO,UriComponentsBuilder uriComponentsBuilder) {
		Collection collection = collectionRepository.save(new Collection(createCollectionDTO, userRepository.findByUsername(createCollectionDTO.user())));
		ResponseCollectionDTO response = new ResponseCollectionDTO(collection.getId(),collection.getName(),collection.getDescription(),collection.getImage());
		URI url = uriComponentsBuilder.path("/${id}").buildAndExpand(collection.getId()).toUri();
		return ResponseEntity.created(url).body(response);
	}
	
	@PostMapping("/profile/{username}/played")
	public ResponseEntity addPlayed(@RequestBody @Valid AddPlayedDto playedDto, @PathVariable("username") String username) {
		Played played = playedRepository.save(new Played(userRepository.findByUsername(username),playedDto));
		return ResponseEntity.ok("Game Added");
	}
	
	@PostMapping("/profile/{username}/playing")
	public ResponseEntity addPlaying(@RequestBody @Valid AddPlayingDto playingDto, @PathVariable("username") String username) {
		Playing playing = playingRepository.save(new Playing(playingDto,userRepository.findByUsername(username)));
		return ResponseEntity.ok("Game added to playing");
	}
	
	@GetMapping("/game")
	public ResponseEntity searchGame(@RequestParam String name) {
		var response = service.searchGame(name);
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/publisher")
	public ResponseEntity searchcharacter(@RequestParam String character) {
		var response = service.searchCharacter(character);
		return ResponseEntity.ok(response);
	}
}
