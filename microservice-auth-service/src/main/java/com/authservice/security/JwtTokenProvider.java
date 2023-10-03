package com.authservice.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.authservice.model.User;
import com.authservice.model.UserRole;
import com.authservice.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Autowired
	private UserRepository userRepository;

	private static final String SECRET_KEY = "!@#$%^&*12345SecretKey6789*&^%$#@!";
	private static final int TOKEN_VALIDITY = 3600 * 5;
	private static final String AUTHENTICATED = "authenticated";

	public String generateJwtToken(User user) {
		String rolesString = user.getRoles().stream().findFirst().map(UserRole::getRoleName).orElse("");

		String jwtToken = "";
		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() * TOKEN_VALIDITY + 20000);
		jwtToken = Jwts.builder().setSubject(user.getEmail())
				.claim("role", rolesString)
				.claim("userId", user.getId())
				.setIssuedAt(issuedAt)
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();

		return jwtToken;
	}

	public void validateJwtToken(String token) {
		Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Date getExpirationDateFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		return claims.getExpiration();
	}

	public String getUserFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public User userDetails(String userName) {
		return userRepository.findByEmail(userName);
	}

	public boolean validateToken(String jwtToken, User userDetails) {
		final String username = getUserFromToken(jwtToken);
		return (username.equals(userDetails.getEmail()) && !isTokenExpired(jwtToken));
	}
}
