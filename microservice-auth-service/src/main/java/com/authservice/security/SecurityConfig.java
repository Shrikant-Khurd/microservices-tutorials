package com.authservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/*
	 * @Bean public JwtFilter authenticationJwtTokenFilter() { return new
	 * JwtFilter(); }
	 */

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	private JwtFilter authenticationJwtTokenFilter;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
//		.cors().and().csrf().disable()
				// .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeHttpRequests().anyRequest().permitAll()
				/*.requestMatchers("/api/auth/authenticate", "/api/auth/add-user", "/api/auth/validate","/api/auth/current-userRole").permitAll()

				 * .requestMatchers(HttpMethod.POST, "/api/user/add-role").permitAll()
				 * .requestMatchers(HttpMethod.GET, "/api/user/getlist").hasAuthority("ADMIN")
				 * .requestMatchers(HttpMethod.GET,
				 * "/api/user/restricted").hasAuthority("ADMIN")
				 * .requestMatchers(HttpMethod.GET,
				 * "/api/user/get-user-by-id").hasAnyAuthority("ADMIN", "USER") .anyRequest()
				 * .authenticated()
				 */
				.and().cors().and().httpBasic().and().csrf().disable();

		// http.authenticationProvider(authenticationProvider());

		// http.addFilterBefore(authenticationJwtTokenFilter,
		// BasicAuthenticationFilter.class);
		http.addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

	/*	http.cors().and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().csrf().disable().formLogin().disable()
		.httpBasic().disable()
		.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and().authorizeRequests().anyRequest().permitAll();
*/
		return http.build();
	}
}
