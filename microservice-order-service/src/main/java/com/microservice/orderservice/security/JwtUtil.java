package com.microservice.orderservice.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	public static String CURRENT_USER = "";
	public static long CURRENT_USER_ID;
	public static String JWTToken;

	private static final String SECRET_KEY = "!@#$%^&*12345SecretKey6789*&^%$#@!";

	public static String GetJwtToken() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String jwtToken = request.getHeader("Authorization");
		return getJwtFromRequest(jwtToken);
//		return getJwtFromRequest(jwtToken);

		// return request.getHeader("Authorization");
	}

	public static String getJwtFromRequest(String token) {
//		List<String> authHeaders = request.getHeaders().getOrEmpty("Authorization");
		if (!token.isEmpty()) {
			String bearerToken = token;
			if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
				return bearerToken.substring(7, bearerToken.length());
			}
		}
		return null;
	}

	public String getUserFromToken() {

		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(JWTToken).getBody();
		CURRENT_USER = claims.getSubject();
		return CURRENT_USER;
	}

	public static Long getUserIdFromToken() {
		JWTToken = GetJwtToken();
		System.out.println(JWTToken);
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(JWTToken).getBody();
		// CURRENT_USER_ID = (Long) claims.get("userId");
		CURRENT_USER_ID = claims.get("userId", Integer.class).longValue();
		System.out.println("UserId : " + CURRENT_USER_ID);
		return CURRENT_USER_ID;
	}

	public static Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		CURRENT_USER_ID = (long) claims.get("userId");
		return CURRENT_USER_ID;
	}

}
