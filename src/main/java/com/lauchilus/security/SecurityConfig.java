package com.lauchilus.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

//@CrossOrigin("http://localhost:4200")
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig{

	@Autowired
	private SecurityFilter securityFilter;
	
	@Autowired
	private CorsFilter corsFilter;
	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		System.out.println("LA POUTA QUE TE PARIO");

//		return httpSecurity.csrf(csrf -> csrf.disable())
//				.authorizeHttpRequests(
//				authz -> authz.requestMatchers("/**").permitAll()).build();

		return httpSecurity.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						authz -> authz.requestMatchers("auth/login","auth/register").permitAll().anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(corsFilter, ChannelProcessingFilter.class).build();
	}
	
	
  


	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}



}
