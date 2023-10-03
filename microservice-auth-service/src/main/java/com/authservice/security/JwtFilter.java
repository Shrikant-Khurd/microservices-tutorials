package com.authservice.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authservice.dto.ApiResponse;
import com.authservice.model.User;
import com.authservice.model.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	 public static String CURRENT_USER = "";
	 public static  long CURRENT_USER_ID ;
	 public static Set<UserRole> CURRENT_USER_ROLE;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		String requestTokenHeader = extractToken(request);
		String userName = null;

		try {
			if (requestTokenHeader != null && jwtTokenProvider.validateToken(requestTokenHeader)) {
				userName = jwtTokenProvider.getUserFromToken(requestTokenHeader);
				CURRENT_USER = userName;
			}
		} catch (SignatureException e) {
			handleInvalidTokenResponse(response, request);
			return;
		} catch (ExpiredJwtException e) {
			handleExpiredTokenResponse(response, request, e.getMessage());
			return;
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			User userDetails = jwtTokenProvider.userDetails(userName);
			CURRENT_USER_ID = userDetails.getId();
			CURRENT_USER_ROLE=userDetails.getRoles();
			List<GrantedAuthority> authorities = getUserAuthority(userDetails.getRoles());
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private List<GrantedAuthority> getUserAuthority(Set<UserRole> userRoles) {
		Set<GrantedAuthority> roles = new HashSet<>();
		userRoles.forEach(role -> roles.add(new SimpleGrantedAuthority(role.getRoleName())));
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
		return grantedAuthorities;
	}

	private void handleExpiredTokenResponse(HttpServletResponse response, HttpServletRequest request, String msg)
			throws IOException {
		String requestPath = request.getServletPath();
		ApiResponse expiredTokenResponse = new ApiResponse();
		expiredTokenResponse.setTimeStamp(new Date());
		expiredTokenResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
		expiredTokenResponse.setHttpStatusCode(HttpStatus.UNAUTHORIZED.value());
		expiredTokenResponse.setMessage("Session is expired. Please try logging in again.");
		expiredTokenResponse.setPath(requestPath);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ObjectMapper objectMapper = new ObjectMapper();
		String errorResponse = objectMapper.writeValueAsString(expiredTokenResponse);
		response.getWriter().write(errorResponse);
	}

	private void handleInvalidTokenResponse(HttpServletResponse response, HttpServletRequest request)
			throws IOException {

		String requestPath = request.getRequestURI();
		ApiResponse expiredTokenResponse = new ApiResponse();
		expiredTokenResponse.setTimeStamp(new Date());
		expiredTokenResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
		expiredTokenResponse.setHttpStatusCode(HttpStatus.UNAUTHORIZED.value());
		expiredTokenResponse.setMessage("Invalid JWT token");
		expiredTokenResponse.setPath(requestPath);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ObjectMapper objectMapper = new ObjectMapper();
		String errorResponse = objectMapper.writeValueAsString(expiredTokenResponse);
		response.getWriter().write(errorResponse);
	}
}
