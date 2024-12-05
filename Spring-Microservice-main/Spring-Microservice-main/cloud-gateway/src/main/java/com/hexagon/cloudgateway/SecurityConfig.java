//package com.hexagon.cloudgateway;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//	@Bean
//	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
//		http.cors().and().csrf().disable().authorizeExchange().pathMatchers("/**").permitAll().pathMatchers("/")
//				.permitAll().anyExchange().authenticated().and().headers().frameOptions().disable();
//
//		return http.build();
//	}
//
//}