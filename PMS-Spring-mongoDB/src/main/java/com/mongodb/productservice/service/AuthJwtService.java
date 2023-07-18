package com.mongodb.productservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mongodb.productservice.dto.LoginRequest;
import com.mongodb.productservice.dto.LoginResponse;
import com.mongodb.productservice.model.UserRole;
import com.mongodb.productservice.repository.UserRepository;
import com.mongodb.productservice.security.JwtTokenProvider;

@Service
public class AuthJwtService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtTokenProvider jwtUtil;

	public LoginResponse createJwtToken(LoginRequest jwtRequest) {

		UserDetails userDetails = loadUserByUsername(jwtRequest.getEmail());
		String newGeneratedToken = jwtUtil.generateJwtToken(userDetails);

		com.mongodb.productservice.model.User user = userRepository.findByEmail(jwtRequest.getEmail());

		return new LoginResponse(newGeneratedToken, user.getId(), user.getRoles());
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		com.mongodb.productservice.model.User user = userRepository.findByEmail(email);
		if (user != null) {
			List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
			return buildUserForAuthentication(user, authorities);
		} else {
			throw new UsernameNotFoundException("username not found");
		}
	}

	private List<GrantedAuthority> getUserAuthority(Set<UserRole> userRoles) {
		Set<GrantedAuthority> roles = new HashSet<>();
		userRoles.forEach((role) -> {
			roles.add(new SimpleGrantedAuthority(role.getRoleName()));
		});

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
		return grantedAuthorities;
	}

	private UserDetails buildUserForAuthentication(com.mongodb.productservice.model.User user,
			List<GrantedAuthority> authorities) {
		return new User(user.getEmail(), user.getPassword(), authorities);
	}
}
