package com.mongodb.productservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mongodb.productservice.service.AuthJwtService;

@Configuration
@EnableWebSecurity
public class JwtWebSecurityConfiguration {

	//@Autowired
	//private  AuthEntryPointJwt unauthorizedHandler;
	@Autowired
    private  JwtTokenProvider jwtTokenProvider;
	//@Autowired
	//private  AuthJwtService userDetailsService;

	@Bean
	public AuthJwtService userDetailsService() {
		return new AuthJwtService();
	}
	@Bean
	public AuthEntryPointJwt unauthorizedHandler() {
		return new AuthEntryPointJwt();
	}
	@Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }
	
	/*public JwtWebSecurityConfiguration(AuthEntryPointJwt unauthorizedHandler, AuthJwtService userDetailsService) {
	
		this.unauthorizedHandler = unauthorizedHandler;
		this.userDetailsService = userDetailsService;
	}*/

	@Bean
	public JwtAuthenticationFilter authenticationJwtTokenFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder()); // Add the password encoder
		return authProvider;
	}

	// @Autowired
	/*
	 * public void configureGlobal(AuthenticationManagerBuilder auth) throws
	 * Exception { auth.authenticationProvider(authenticationProvider()); }
	 */

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
				//.exceptionHandling().authenticationEntryPoint(unauthorizedHandler()).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/productMongo/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/productMongo/**").hasAnyAuthority("ADMIN","USER")
				.requestMatchers(HttpMethod.POST, "/api/user/add-role").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/user/getlist").hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.GET, "/api/auth/restricted").hasAuthority("ADMIN")
				
				.requestMatchers(HttpMethod.GET, "/api/user/data/{id}").hasAnyAuthority("ADMIN","USER")
				.anyRequest()
			//	.authenticated();
		 .authenticated().and().cors().and().httpBasic().and().csrf().disable();

		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
