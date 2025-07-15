package com.rong.applicationservice.config;

import com.rong.applicationservice.exception.AuthorizationNotFoundException;
import com.rong.applicationservice.security.JwtUtil;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@Slf4j
public class FeignClientConfig {

    @Bean
    public RequestInterceptor jwtRequestInterceptor(JwtUtil jwtUtil) {
        return requestTemplate -> {
            // Retrieve the token (e.g. from a SecurityContext or other context)
            String jwtToken = jwtUtil.getToken();

            if (jwtToken != null) {
                requestTemplate.header("Authorization", "Bearer " + jwtToken);
            } else {
                throw new AuthorizationNotFoundException("No jwt found");
            }
        };
    }
}
