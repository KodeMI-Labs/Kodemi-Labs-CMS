package com.kodemi.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {

		CorsConfiguration config = new CorsConfiguration();

		// Frontend URL
		config.setAllowedOrigins(List.of("http://localhost:5173"));

		// Allow all headers
		config.setAllowedHeaders(List.of("*"));

		// Allow all HTTP methods
		config.setAllowedMethods(List.of("*"));

		// Expose all headers
		config.setExposedHeaders(List.of("*"));

		// Allow cookies/auth headers if needed
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}
/*
 * @Configuration public class CorsConfig {
 * 
 * @Bean public CorsFilter corsFilter() { CorsConfiguration config = new
 * CorsConfiguration(); config.setAllowCredentials(true);
 * config.setAllowedOriginPatterns(List.of( "http://localhost:*",
 * "https://localhost:*", "https://*.ngrok-free.app",
 * "https://*.ngrok-free.dev", "https://*.ngrok.io", "https://*.ngrok.app",
 * "https://*.ngrok.dev" )); config.setAllowedHeaders(List.of("*"));
 * config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH",
 * "OPTIONS", "HEAD")); config.setExposedHeaders(List.of("Authorization",
 * "Content-Type")); config.setMaxAge(3600L); UrlBasedCorsConfigurationSource
 * source = new UrlBasedCorsConfigurationSource();
 * source.registerCorsConfiguration("/**", config); return new
 * CorsFilter(source); } }
 */
