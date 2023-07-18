package com.mongodb.productservice.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mongodb.productservice.exception.EmailAlreadyExistException;
import com.mongodb.productservice.model.User;
import com.mongodb.productservice.model.UserRole;
import com.mongodb.productservice.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Autowired
	private UserRepository userRepository;

	private static final String SECRET_KEY = "!@#$%^&*12345SecretKey6789*&^%$#@!";
	private static final int TOKEN_VALIDITY = 3600 * 5;
	private static final String AUTHENTICATED = "authenticated";

	/*public Map<String, String> generateJwtToken(User user) {
		String msg = "Login Successful";
		String jwtToken = "";
		Set<UserRole> rolesSet = new HashSet<>(user.getRoles()); // Convert roles to a set
		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() + 20000);
		jwtToken = Jwts.builder().setSubject(user.getEmail()).claim("role", rolesSet).claim("userId", user.getId())
				.setIssuedAt(issuedAt).setExpiration(expiration).signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
		Map<String, String> jwtTokenGen = new HashMap<>();
		jwtTokenGen.put("token", jwtToken);
		jwtTokenGen.put("message", msg);
		jwtTokenGen.put("userRole", rolesSet.toString());
		jwtTokenGen.put("userId", Long.toString(user.getId()));
		return jwtTokenGen;
	}*/
	public String generateJwtToken(User user) {
		
		String jwtToken = "";
	//	Set<UserRole> rolesSet = new HashSet<>(user.getRoles()); // Convert roles to a set
		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime()*TOKEN_VALIDITY + 20000);
		jwtToken = Jwts.builder()
				.setSubject(user.getEmail())
				.claim("role", user.getRoles())
				.claim("userId", user.getId())
				.setIssuedAt(issuedAt)
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();

		return jwtToken;
	}

	public boolean validateToken(String token) {

		// Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		// return true;
		 return  !isTokenExpired(token);

		/*try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			return true;
		} catch (EmailAlreadyExistException ex) {
			throw new EmailAlreadyExistException(
					messageSource.getMessage("api.error.user.already.registered", null, Locale.ENGLISH));
		}*/

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
