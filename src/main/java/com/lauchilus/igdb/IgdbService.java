package com.lauchilus.igdb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.igdb.apicalypse.APICalypse;
import com.api.igdb.apicalypse.Sort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IgdbService {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpHeaders headers = new HttpHeaders();;

//	private final IGDBWrapper wrapper;
//	 public IgdbService(IGDBWrapper wrapper) {
//	        this.wrapper = wrapper;
//	    }

//	public String hacerSolicitud() throws RequestException {
//		String json = wrapper.apiJsonRequest(Endpoints.GAMES, "fields name; limit 10");
//		return json;
//	}

	public String listar() {
		// Crea el cuerpo de la solicitud
		String requestBody = "fields name; limit 10;";

		// Crea una HttpEntity con los encabezados y el cuerpo
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity response = restTemplate.exchange("https://api.igdb.com/v4/games/", HttpMethod.POST, httpEntity,
				String.class);

		return response.getBody().toString();
	}

//	public String searchGame(SearchRequestDTO search) {
//
//		APICalypse apiCalypse = new APICalypse()
//				.fields("name, game,published_at")
//				.limit(15).search(search.name());
//		String requestBody = apiCalypse.buildQuery();
//		
//		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
//		ResponseEntity response = restTemplate.exchange("https://api.igdb.com/v4/search", HttpMethod.POST, httpEntity,
//				String.class);
//		return response.toString();
//	}

	public String searchGame(String game) {
		APICalypse apiCalypse = new APICalypse().fields("summary,name,aggregated_rating,category, screenshots.image_id,collection")
				.search(game).limit(1).where("category=0");
		String requestBody = apiCalypse.buildQuery();

		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity response = restTemplate.exchange("https://api.igdb.com/v4/games/", HttpMethod.POST, httpEntity,
				String.class);
		return response.getBody().toString();
	}

	public byte[] processImage(String url) throws IOException {
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
				byte[].class);
		System.out.println(response);
		return response.getBody();
	}

	public String getCollection(String string) {
		APICalypse apiCalypse = new APICalypse().fields("name").where("id=" + string);
		String requestBody = apiCalypse.buildQuery();
		System.out.println(requestBody);
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity response = restTemplate.exchange("https://api.igdb.com/v4/collections", HttpMethod.POST,
				httpEntity, String.class);
		System.out.println(response.getBody().toString());
		return response.getBody().toString();
	}

	public Integer searchGameId(String name) throws IOException {
		APICalypse apiCalypse = new APICalypse().fields("id").search(name);
		String requestBody = apiCalypse.buildQuery();

		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity response = restTemplate.exchange("https://api.igdb.com/v4/search", HttpMethod.POST, httpEntity,
				String.class);
		String responseBody = response.getBody().toString();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);

		int id = jsonNode.get(0).get("id").asInt();
		return id;
	}

	public String searchGameById(Integer id) throws IOException {
		APICalypse apiCalypse = new APICalypse().fields("*").where("id = " + id);
		String requestBody = apiCalypse.buildQuery();
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity response = restTemplate.exchange("https://api.igdb.com/v4/games/", HttpMethod.POST, httpEntity,
				String.class);

		return response.getBody().toString();
	}

	public Object searchCharacter(String name) {
		APICalypse apiCalypse = new APICalypse().fields("character").search(name);
		String requestBody = apiCalypse.buildQuery();

		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity response = restTemplate.exchange("https://api.igdb.com/v4/search", HttpMethod.POST, httpEntity,
				String.class);
		return response.toString();
	}
	
	public String listGames() {
		APICalypse apiCalypse = new APICalypse().fields("name,storyline,follows,screenshots.image_id").limit(10);
		String requestBody = apiCalypse.buildQuery();
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity response = restTemplate.exchange("https://api.igdb.com/v4/games/", HttpMethod.POST, httpEntity,
				String.class);
		return response.getBody().toString();
		
	}

}
//		String requestBody = "fields *; search \"" + name + "\"; limit 50;";
//		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
//		ResponseEntity response = restTemplate.exchange("https://api.igdb.com/v4/search", HttpMethod.POST, httpEntity,
//				String.class);
//		
//		return response.getBody().toString();