package com.lauchilus.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
////
//@Configuration
//@EnableWebSecurity
public class CorsConfig{

//	 @Override
//	    public void addCorsMappings(CorsRegistry registry) {
//	        registry.addMapping("/**")
//	                .allowedOrigins("http://localhost:4200")
//	                .allowedMethods("*")
//	                .allowedHeaders("*")
//	                .exposedHeaders("*")
//	                .allowCredentials(true);
//	    }
//	 
//	 @Bean
//		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//			http
//				// by default uses a Bean by the name of corsConfigurationSource
//				.cors(cors -> cors.disable());
//			return http.build();
//		}
	 
//	 @Bean
//	    CorsConfigurationSource corsConfigurationSource() {
//	        CorsConfiguration configuration = new CorsConfiguration();
//	        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//	        configuration.setAllowedMethods(Arrays.asList("GET","POST","DELETE"));
//	        configuration.setAllowedHeaders(List.of("*"));
//	        configuration.setAllowCredentials(true);
//	        configuration.setExposedHeaders(Arrays.asList("*"));
//	        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        source.registerCorsConfiguration("/**", configuration);
//	        return source;
//	    }
	 
//	 @Bean
//	    public CorsFilter corsFilter() {
//	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        CorsConfiguration config = new CorsConfiguration();
//	        config.setAllowCredentials(true);
//	        config.addAllowedOrigin("http://localhost:4200");
//	        config.addAllowedHeader("*");
//	        config.addAllowedMethod("*");
//	        source.registerCorsConfiguration("/**", config);
//	        return new CorsFilter(source);
//	    }

//	
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")
//				.allowedOrigins("http://localhost:4200") // Ajusta los orígenes permitidos
//				.allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
//	}
	
	
//	@Bean
//	public CorsFilter corsFilter() {
//
//	    CorsConfiguration corsConfiguration = new CorsConfiguration();
//	    corsConfiguration.addAllowedOrigin("http://localhost:4200/**"); // Reemplaza con la URL de tu frontend
//	    corsConfiguration.addAllowedMethod("OPTIONS");
//	    corsConfiguration.addAllowedMethod("GET");
//	    corsConfiguration.addAllowedMethod("POST");
//	    corsConfiguration.addAllowedMethod("PUT");
//	    corsConfiguration.addAllowedMethod("DELETE");
//	    corsConfiguration.addAllowedHeader("authorization");
//	    corsConfiguration.addAllowedHeader("content-Type");
//	    corsConfiguration.
//	    
//	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	    source.registerCorsConfiguration("/**", corsConfiguration);
//
//	    CorsFilter corsFilter = new CorsFilter(source);
//	    
//	    // Agregar un registro de depuración
//	    System.out.println("Configuración de CORS cargada.");
//	    
//	    return corsFilter;
//	}

////	
	
}
