package com.mongodb.productservice.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mongodb.productservice.dto.ApiResponse;
import com.mongodb.productservice.exception.EmailAlreadyExistException;
import com.mongodb.productservice.model.User;
import com.mongodb.productservice.repository.UserRepository;
import com.mongodb.productservice.utils.ConstantMethods;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Autowired
	private UserRepository userRepository;

	private static final String SECRET_KEY = "!@#$%^&*12345SecretKey6789*&^%$#@!";
	private static final int TOKEN_VALIDITY = 3600 * 5;
	private static final String AUTHENTICATED = "authenticated";
	// private Key key;
	private SecretKey key;

	

	public String generateJwtToken(UserDetails userDetails) {
		User user = userRepository.findByEmail(userDetails.getUsername());

		// key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	//	byte[] apiKeySecretBytes = SECRET_KEY.getBytes();
	//	key = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

		key=Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
		System.out.println("Secret key : " + key);

		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() + TOKEN_VALIDITY * 10000);

		// key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
		return Jwts.builder()
				.setSubject((userDetails.getUsername()))
				.setId(Long.toString(user.getId()))
				.claim("UserId", user.getId())
				.claim("userRole", user.getRoles())
				.setIssuedAt(issuedAt)
				.setExpiration(expiration)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
//		.signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
//		.signWith(key).compact();
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = parseClaims(token).getBody();
		return Long.parseLong(claims.getId());
	}

	public String getUsernameFromToken(String token) {
		Claims claims = parseClaims(token).getBody();
		return claims.getSubject();
	}

	public Boolean isAuthenticated(String token) {
		Claims claims = parseClaims(token).getBody();
		return claims.get(AUTHENTICATED, Boolean.class);
	}

	private Jws<Claims> parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}

	/*
	 * public boolean validateToken(String authToken) { try {
	 * parseClaims(authToken); return true; } catch (MalformedJwtException ex) {
	 * logger.error("Token expired: {}", ex.getMessage()); return false; } }
	 */
	public boolean validateToken(String authToken) throws EmailAlreadyExistException {
		try {
			Jws<Claims> claimsJws = parseClaims(authToken);
			Claims claims = claimsJws.getBody();
			Date expiration = claims.getExpiration();
			Date now = new Date();
			if (expiration.before(now)) {
				throw new IllegalArgumentException("Token has expired");
			}
			return true;
		} catch (EmailAlreadyExistException ex) {
			logger.error("Invalid token: {}", ex.getMessage());
			//ApiResponse failResponse =ConstantMethods.failureResponse(ex.getMessage(),HttpStatus.CONFLICT);
			return false;
		}
	}

	/*
	 * public String generateToken(Authentication auth) { AuthJwtService user =
	 * (AuthJwtService) auth.getPrincipal();
	 * 
	 * return Jwts.builder() .setId(user.getId().toString())
	 * .setSubject(user.getUsername()) .signWith(key) .compact(); }
	 */
}
