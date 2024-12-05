//package com.hexagon.cloudgateway;
//
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//
//@Configuration
//public class CorsConfig {
//
//	@Bean
//	public CorsWebFilter corsWebFilter() {
//		CorsConfiguration corsConfig = new CorsConfiguration();
//		corsConfig.setAllowedOriginPatterns(List.of("*"));
//		corsConfig.setAllowedMethods(List.of("PUT", "GET", "POST", "DELETE", "OPTIONS"));
//		corsConfig.setAllowedHeaders(List.of("Content-Type: Application/json", "Authorization"));
//		corsConfig.setAllowCredentials(true);
//		corsConfig.setMaxAge(3600L);
//		return new CorsWebFilter(exchange -> corsConfig);
//	}
//}
