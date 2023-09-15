package com.lauchilus.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:4200") // Ajusta los orígenes permitidos
				.allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
	}

	@Bean
	public CorsFilter corsFilter() {

		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("http://localhost:4200"); // Reemplaza con la URL de tu frontend
		corsConfiguration.addAllowedMethod("OPTIONS");//
		corsConfiguration.addAllowedMethod("GET");
		corsConfiguration.addAllowedMethod("POST");
		corsConfiguration.addAllowedMethod("PUT");
		corsConfiguration.addAllowedMethod("DELETE");
		corsConfiguration.addAllowedHeader("Authorization");
		corsConfiguration.addAllowedHeader("Content-Type");
		corsConfiguration.addAllowedHeader("Access-Control-Allow-Origin");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);

		CorsFilter corsFilter = new CorsFilter(source);
		// Agregar un registro de depuración
		System.out.println("Configuración de CORS cargada.");
		return corsFilter;
//    }
//        
//        return new CorsFilter(source);
	}
	
	
}
