package com.lauchilus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lauchilus.DTO.RegisterDto;
import com.lauchilus.DTO.UserRegisterDTO;
import com.lauchilus.entity.User;
import com.lauchilus.repository.UserRepository;
import com.lauchilus.security.DatosJWTToken;
import com.lauchilus.security.TokenService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@PostMapping("/login")
	public ResponseEntity autenticarUsuario(@RequestBody @Valid UserRegisterDTO user) {
		Authentication authToken = new UsernamePasswordAuthenticationToken(user.username(), user.password());
		var usuarioAutenticado = authenticationManager.authenticate(authToken );
		var JWTtoken = tokenService.generateToken((User) usuarioAutenticado.getPrincipal());
		return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
		if(userRepository.existsByUsername(registerDto.username())) {
			return new ResponseEntity<>("Username is taken! ", HttpStatus.BAD_REQUEST);
		}
		
		User user = new User();
		user.setUsername(registerDto.username());
		user.setPassword(passwordEncoder.encode(registerDto.password()));
		userRepository.save(user);
		return ResponseEntity.ok("User Registered success!");
	}
	
	
}
