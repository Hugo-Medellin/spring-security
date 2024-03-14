package com.portal.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.portal.security.filter.JwtAuthenticationFilter;
import com.portal.security.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {

	@Autowired
	AuthenticationConfiguration authenticationConfiguration;

	@Bean
	AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private static final Logger LOG = LoggerFactory.getLogger(SpringSecurityConfig.class);

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		LOG.info("filterChain");
		return httpSecurity.authorizeHttpRequests( (authz) -> authz
				.requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()
				.anyRequest().authenticated())
				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtValidationFilter(authenticationManager()))
				.csrf(config -> config.disable() )
				.sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
	}

}
