package com.lauchilus.security;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lauchilus.entity.User;


@Service
public class TokenService {

//	@Value("${api.security.secret}")
	private String apiSecret = "123456";

	public String generateToken(User user) {
		System.out.println(user.getUsername());
		try {
		    Algorithm algorithm = Algorithm.HMAC256(apiSecret);
		    return JWT.create()
		        .withIssuer("game list")
		        .withSubject(user.getUsername())
		        .withClaim("id",user.getId())
		        .withExpiresAt(generateExpireDate())
		        .sign(algorithm);

		} catch (JWTCreationException exception){
			throw new RuntimeException();
		}
	}
	
	 public String getSubject(String token) {
	        if (token == null) {
	            throw new IllegalArgumentException("El token es nulo");
	        }
	        try {
	            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
	            DecodedJWT verifier = JWT.require(algorithm)
	                .withIssuer("game list")
	                .build()
	                .verify(token);
	            return verifier.getSubject();
	        } catch (JWTVerificationException exception) {
	            throw new RuntimeException("Error al verificar el token JWT", exception);
	        }
	    }


//	public String getSubject(String token) {
//		if(token==null) {
//			throw new RuntimeException();
//		}
//		DecodedJWT verifier = null;
//		try {
//		    Algorithm algorithm = Algorithm.HMAC256(apiSecret);
//		    verifier = JWT.require(algorithm)
//		        .withIssuer("game list")
//		        .build()
//		        .verify(token);
//		    verifier.getSubject();
//		    System.out.println("LA PUTA MDRE 2");
//		} catch (JWTVerificationException exception){
//		    System.out.println(exception.toString());
//		}
//		if(verifier.getSubject()==null) {
//			throw new RuntimeException("Verifier invalido");
//		}
//		return verifier.getSubject();
//
//	}

	


	private Instant generateExpireDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
	}
}
