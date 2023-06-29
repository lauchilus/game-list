package com.lauchilus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lauchilus.DTO.UserRegisterDTO;
import com.lauchilus.entity.User;
import com.lauchilus.security.DatosJWTToken;
import com.lauchilus.security.TokenService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/login")
public class AutenticacionController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity autenticarUsuario(@RequestBody @Valid UserRegisterDTO user) {
		Authentication authToken = new UsernamePasswordAuthenticationToken(user.username(), user.password());
		var usuarioAutenticado = authenticationManager.authenticate(authToken );
		var JWTtoken = tokenService.generateToken((User) usuarioAutenticado.getPrincipal());
		return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
		
	}
	
	
}
