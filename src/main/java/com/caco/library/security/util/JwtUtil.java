package com.caco.library.security.util;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${token.expiration.time}")
	private long tokenExpirationTime;

	private SecretKey key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public String extractUsername(String token) {
		return Jwts.parser().verifyWith(key).build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}
}
