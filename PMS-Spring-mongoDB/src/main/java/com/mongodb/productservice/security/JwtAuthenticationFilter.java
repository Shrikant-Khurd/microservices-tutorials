package com.mongodb.productservice.security;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mongodb.productservice.exception.EmailAlreadyExistException;
import com.mongodb.productservice.model.UserRole;
import com.mongodb.productservice.service.AuthJwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private AuthJwtService userDetailsService;
	@Autowired
	private MessageSource messageSource;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String token = extractToken(request);
		
		
		
		if (token != null && jwtTokenProvider.validateToken(token)) {
			String username = jwtTokenProvider.getUsernameFromToken(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	
	/*	else {
			throw new EmailAlreadyExistException(
					messageSource.getMessage("api.error.user.already.registered", null, Locale.ENGLISH));
	
		}*/
		// try {
		chain.doFilter(request, response);
		// } catch (IOException | ServletException e) {
		// logger.error("Token expired: {}", e.getMessage());
		// }
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	/*
	 * @Override protected void doFilterInternal(HttpServletRequest request,
	 * HttpServletResponse response, FilterChain filterChain) throws
	 * ServletException, IOException { try { String jwt =
	 * getJwtFromRequest(request);
	 * 
	 * if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
	 * 
	 * Long userId = jwtTokenProvider.getUserIdFromToken(jwt); UserDetails
	 * userDetails = userDetailsService.loadUserById(userId);
	 * 
	 * String username = jwtTokenProvider.getUsernameFromToken(jwt); UserDetails
	 * userDetails1 = userDetailsService.loadUserByUsername(username);
	 * 
	 * Collection<? extends GrantedAuthority> authorities =
	 * jwtTokenProvider.isAuthenticated(jwt) ? userDetails.getAuthorities() :
	 * List.of(new SimpleGrantedAuthority(UserRole.ROLE_PRE_VERIFICATION_USER));
	 * UsernamePasswordAuthenticationToken authentication = new
	 * UsernamePasswordAuthenticationToken(userDetails, null, authorities);
	 * authentication.setDetails(new
	 * WebAuthenticationDetailsSource().buildDetails(request));
	 * 
	 * SecurityContextHolder.getContext().setAuthentication(authentication); } }
	 * catch (Exception ex) {
	 * logger.error("Could not set user authentication in security context", ex); }
	 * 
	 * filterChain.doFilter(request, response); }
	 * 
	 * private String getJwtFromRequest(HttpServletRequest request) { String
	 * bearerToken = request.getHeader("Authorization"); if
	 * (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
	 * return bearerToken.substring(7, bearerToken.length()); } return null; }
	 */
}
