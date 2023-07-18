package com.authservice.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authservice.model.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, //
			HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// final HttpServletRequest request = (HttpServletRequest) servletRequest;
		// final HttpServletResponse response = (HttpServletResponse) servletResponse;
	/*	String token = extractToken(request);



		if (jwtTokenProvider.validateToken(token)) {
			User userDetails = jwtTokenProvider.getUserFromToken(token);
			List<GrantedAuthority> authorities = getUserAuthority(userDetails.getRoles());
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} else {
			System.out.println(token);
			throw new BadCredentialsException("Invalid JWT token");
		}*/
		
		final String authHeader = request.getHeader("authorization");
		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			filterChain.doFilter(request, response);
		} else {
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				throw new ServletException("An exception occurred");
			}
		}
		final String token = authHeader.substring(7);
		Claims claims = Jwts.parser().setSigningKey("!@#$%^&*12345SecretKey6789*&^%$#@!").parseClaimsJws(token)
				.getBody();
		request.setAttribute("claims", claims);
		//request.setAttribute("blog", servletRequest.getParameter("id"));
		
		
	//	User userDetails = jwtTokenProvider.getUserFromToken(token);
	//	List<GrantedAuthority> authorities = getUserAuthority(userDetails.getRoles());
	//	Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
	//	SecurityContextHolder.getContext().setAuthentication(authentication);


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
		userRoles.forEach((role) -> {
			roles.add(new SimpleGrantedAuthority(role.getRoleName()));
		});

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
		return grantedAuthorities;
	}

}
