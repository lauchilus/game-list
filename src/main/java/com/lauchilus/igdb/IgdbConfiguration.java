package com.lauchilus.igdb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.TwitchAuthenticator;
import com.api.igdb.utils.TwitchToken;

@Configuration
public class IgdbConfiguration {

	private String apiToken;

	private String apiClientId = "mk5fdnm6nd2vzxr4uxv2aoblpxjlof";

	@Bean
	HttpHeaders apiHeaders() {
		String apiClientId = "mk5fdnm6nd2vzxr4uxv2aoblpxjlof";
		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
		TwitchToken token = tAuth.requestTwitchToken(apiClientId, "jcpvyjqv6qw2g5mm64iauumh81qcro");
		apiToken = token.getAccess_token();
		IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
		wrapper.setCredentials(apiClientId, apiToken);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer "+apiToken);
		headers.set("Client-ID", apiClientId);

		return headers;
	}



//	@Bean
//	HttpHeaders apiHeaders() {
//		String apiClientId = "mk5fdnm6nd2vzxr4uxv2aoblpxjlof";
//		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
//		TwitchToken token = tAuth.requestTwitchToken(apiClientId, "j3arcuyfvmix1nojumiw0imdc9uxb9");
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
