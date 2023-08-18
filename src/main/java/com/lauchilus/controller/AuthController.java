package com.lauchilus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lauchilus.DTO.RegisterDto;
import com.lauchilus.DTO.UserRegisterDTO;
import com.lauchilus.entity.User;
import com.lauchilus.repository.UserRepository;
import com.lauchilus.security.DatosJWTToken;
import com.lauchilus.security.TokenService;

import jakarta.validation.Valid;


@Controller
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
	
	
//	@GetMapping("/login")
//    public String showLoginPage() {
//        return "login"; // Nombre de la plantilla de login
//    }
//
//    @GetMapping("/register")
//    public String showRegistrationPage() {
//        return "register"; // Nombre de la plantilla de registro
//    }
	
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
