package com.kodemi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	    http.csrf(csrf -> csrf.disable())
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .authorizeHttpRequests(auth -> auth

	                    // =====================================================
	                    // Allow Preflight Requests (Required for CORS)
	                    // =====================================================
	                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

	                    // =====================================================
	                    // PUBLIC COURSE REVIEW APIs
	                    // =====================================================
	                    .requestMatchers(HttpMethod.GET, "/course-review/**", "/courses/*/reviews",
	                            "/courses/*/rating-summary", "/course-rating-summary/**")
	                    .permitAll()

	                    // =====================================================
	                    // PUBLIC INSTRUCTOR REVIEW APIs
	                    // =====================================================
	                    .requestMatchers(HttpMethod.GET, "/instructor-review/**", "/instructor-rating-summary/**")
	                    .permitAll()

	                    // =====================================================
	                    // CREATE COURSE REVIEWS
	                    // =====================================================
	                    .requestMatchers(HttpMethod.POST, "/courses/*/reviews", "/course-review/create",
	                            "/course-review/create-dto", "/course-rating-summary/create",
	                            "/course-rating-summary/create-dto/*")
	                    .authenticated()

	                    // =====================================================
	                    // CREATE INSTRUCTOR REVIEWS
	                    // =====================================================
	                    .requestMatchers(HttpMethod.POST, "/instructor-review/create", "/instructor-review/create-dto",
	                            "/instructor-rating-summary/create", "/instructor-rating-summary/create-dto/*")
	                    .authenticated()

	                    // =====================================================
	                    // UPDATE REVIEW APIs
	                    // =====================================================
	                    .requestMatchers(HttpMethod.PUT, "/course-review/update/**", "/course-rating-summary/update/**",
	                            "/instructor-review/update/**", "/instructor-rating-summary/update/**")
	                    .authenticated()

	                    // =====================================================
	                    // DELETE REVIEW APIs
	                    // =====================================================
	                    .requestMatchers(HttpMethod.DELETE, "/course-review/delete/**",
	                            "/course-rating-summary/delete/**", "/instructor-review/delete/**",
	                            "/instructor-rating-summary/delete/**")
	                    .authenticated()

	                    // =====================================================
	                    // Existing Public APIs
	                    // =====================================================
	                    .requestMatchers(HttpMethod.GET, "/blog/all", "/blog/{blog_Id}", "/blog/verified",
	                            "/blog/trainer/**", "/banner/all", "/banner/{banner_Id}", "/testimonial/all",
	                            "/testimonial/featured", "/testimonial/{testimonial_id}", "/testimonial/status/**")
	                    .permitAll()

	                    .requestMatchers(HttpMethod.GET, "/learner/preference/journey/active",
	                            "/preference/journey/active", "/learner/preference/status/**")
	                    .permitAll()

	                    .requestMatchers("/learner/preference/**").permitAll()

	                    .requestMatchers(HttpMethod.POST, "/contact-us/create", "/contact-us/create-dto").permitAll()

	                    // =====================================================
	                    // Protected Modules & Roles
	                    // =====================================================
	                    .requestMatchers("/api/v1/admin/blogs/**").hasAnyAuthority("SUPER_ADMIN", "CONTENT_ADMIN", "USER_ADMIN")
	                    .requestMatchers("/preference/**").authenticated()

	                    .requestMatchers(HttpMethod.POST, "/blog/create", "/blog/create-dto", "/blog/upload-thumbnail",
	                            "/banner/**", "/testimonial/**")
	                    .authenticated()

	                    .requestMatchers(HttpMethod.PUT, "/banner/**", "/contact-us/**", "/testimonial/**")
	                    .authenticated()

	                    .requestMatchers(HttpMethod.DELETE, "/blog/**", "/banner/**", "/contact-us/**",
	                            "/testimonial/**")
	                    .authenticated()

	                    // =====================================================
	                    // Everything Else (Single Catch-All at the Bottom)
	                    // =====================================================
	                    .anyRequest().authenticated()

	            ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
}