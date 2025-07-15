package com.rong.applicationservice.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            jwtUtil.setToken(token);
            try {
                Jws<Claims> claimsJws = jwtUtil.parseToken(token);
                Claims claims = claimsJws.getBody();

                String username = claims.getSubject();
                String role = claims.get("role", String.class);
                Long userId = claims.get("userId", Long.class);  // Extract userId claim

                if (username != null) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(authority));

                    // Store userId in authentication details for later retrieval
                    authenticationToken.setDetails(userId != null ? userId : new Object());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Note: The above line overwrites details set on previous line, so instead do this:

                    // Correct way: combine userId and request details in a wrapper object, or store userId elsewhere.
                    // For simplicity, just set userId as details:
                    authenticationToken.setDetails(userId);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            } catch (JwtException ex) {
                // Invalid or expired token - optionally log or handle
            }
        }

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}