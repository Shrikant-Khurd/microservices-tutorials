//package com.mongodb.productservice.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
////	@Autowired
////	private BCryptPasswordEncoder bCryptPasswordEncoder;
//	/*
//	 * private CustomMongoUserDetailsService userDetailsService;
//	 * 
//	 * @Autowired public SecurityConfig(CustomMongoUserDetailsService
//	 * userDetailsService) { this.userDetailsService = userDetailsService; }
//	 */
//	@Bean
//	public UserDetailsService mongoUserDetails() {
//		return new CustomMongoUserDetailsService();
//	}
//
//	// @Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		// UserDetailsService userDetailsService = mongoUserDetails();
//		// auth .userDetailsService(userDetailsService)
//		// .passwordEncoder(passwordEncoder());
//		auth.authenticationProvider(authenticationProvider());
//	}
//
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Autowired
//	private CustomMongoUserDetailsService userDetailsService;
//
//	@Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService);
//		authProvider.setPasswordEncoder(passwordEncoder()); // Add the password encoder
//		return authProvider;
//	}
//
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//		/*
//		 * http.httpBasic() .and() .authorizeHttpRequests()
//		 * .requestMatchers(HttpMethod.GET,"/api/user/getlist").permitAll()
//		 * .requestMatchers(HttpMethod.GET,"/api/user/data/{id}").hasRole("ADMIN")
//		 * //.requestMatchers(HttpMethod.POST, "/api/user/**").hasRole("ADMIN") //
//		 * .requestMatchers(HttpMethod.PUT, "/api/user/**").hasRole("ADMIN") //
//		 * .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasRole("ADMIN")
//		 * .and().csrf().disable();
//		 */
//		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeHttpRequests()
//				.requestMatchers(HttpMethod.POST, "/api/auth/authenticate").permitAll()
//				.requestMatchers(HttpMethod.POST, "/api/user/add-user").permitAll()
//				.requestMatchers(HttpMethod.POST, "/api/user/add-role").permitAll()
//				.requestMatchers(HttpMethod.GET, "/api/user/getlist").hasAuthority("ADMIN")
//				.requestMatchers(HttpMethod.POST, "/api/user/add-address/{id}").hasAnyAuthority("ADMIN", "USER")
//				.requestMatchers(HttpMethod.GET, "/api/user/data/{id}").hasAuthority("ADMIN").anyRequest()
//				.authenticated().and().httpBasic().and().csrf().disable();
//
//		return http.build();
//	}
//}
