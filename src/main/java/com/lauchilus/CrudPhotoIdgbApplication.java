package com.lauchilus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.TwitchAuthenticator;
import com.api.igdb.utils.TwitchToken;

@SpringBootApplication
public class CrudPhotoIdgbApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudPhotoIdgbApplication.class, args);
	}
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	
//	@Bean
//	HttpHeaders apiHeaders() {
//		String apiClientId = "mk5fdnm6nd2vzxr4uxv2aoblpxjlof";
//		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
//		TwitchToken token = tAuth.requestTwitchToken(apiClientId, "cml9rfcuks0sheo2f3r81rldg6vuh9");
//		String apiToken = token.getAccess_token();
//		IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
//		wrapper.setCredentials(apiClientId, apiToken);
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.set("Authorization", "Bearer "+apiToken);
//		headers.set("Client-ID", apiClientId);
//
//		return headers;
//	}
}
