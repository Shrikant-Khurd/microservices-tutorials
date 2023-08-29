package com.apigateway.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import reactor.core.publisher.Mono;

@Component
public class JwtUtil {

	private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

	private static final String SECRET_KEY = "!@#$%^&*12345SecretKey6789*&^%$#@!";

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public void validateToken(String token) {
		Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

	}

//	public Set<UserRoleDto> getUserRoleFromToken(String token) {
	public String getUserRoleFromToken(String token) {
		System.out.println(token);
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		System.out.println("user role claims :" + claims);
		System.out.println("user role claims :" + claims.get("role"));

//		List<String> userRoles = (List<String>) claims.get("role");
		// List<UserRoleDto> userRoles = (List<UserRoleDto>) claims.get("role");

		// System.out.println("user role :" + userRoles);
		/*
		 * String currentUserRole = ""; List<Map<String, String>> roleMapList =
		 * (List<Map<String, String>>) claims.get("role");
		 * System.out.println("map role " + roleMapList); for (Map<String, String>
		 * roleMap : roleMapList) { currentUserRole = roleMap.get("roleName");
		 * 
		 * }
		 */

		/*
		 * for (UserRoleDto role : userRoles) { System.out.println("rolessss hvgh " +
		 * role); System.out.println("rolessss " + role);
		 * System.out.println(role.getRoleName()); }
		 */
		String rolesString = (String) claims.get("role");

		System.out.println(rolesString);
		return rolesString;

	}

	public boolean validateJwtToken(String authToken) {
		// return !isTokenValid(authToken);
		/*
		 * if (authToken != null) {
		 * Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken).getBody();
		 * return true; } else { return false; }
		 */
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken).getBody();
			return true;
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (SignatureException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		return false;
	}

	public Boolean isTokenExpired(String token) {
		try {
			final Date expiration = getExpirationDateFromToken(token);
			expiration.before(new Date());
			return true;
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		}
		return false;
	}

	public Date getExpirationDateFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		return claims.getExpiration();

	}

	public String getUserFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

}
