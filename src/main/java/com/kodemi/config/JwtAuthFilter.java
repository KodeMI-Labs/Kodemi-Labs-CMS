package com.kodemi.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		log.info("Incoming URI: {} | Auth Header present: {}", request.getRequestURI(), (authHeader != null));

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				boolean isValid = jwtUtil.validateToken(token);
				log.info("Token validation result: {}", isValid);

				if (isValid) {
					String role = jwtUtil.extractRole(token);
					String userId = jwtUtil.extractUserId(token);

					log.info("Extracted Role: '{}' | Extracted UserId: '{}'", role, userId);

					if (role != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						var authToken = new UsernamePasswordAuthenticationToken(userId, null,
								List.of(new SimpleGrantedAuthority(role)));
						SecurityContextHolder.getContext().setAuthentication(authToken);
						log.info("Successfully set Authentication for user: {} with authority: {}", userId, role);
						
						// Added output statement to verify active authorities
						System.out.println("Extracted Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
						
					} else {
						log.warn("Role is null or SecurityContext already has authentication.");
					}
				}
			} catch (Exception e) {
				log.error("JWT processing failed drastically: {}", e.getMessage(), e);
			}
		}

		filterChain.doFilter(request, response);
	}
}