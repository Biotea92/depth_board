package com.hello.borad.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final static String FRONT_END_LOCAL = "http://localhost:8080";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedMethods("GET", "POST", "HEAD", "PUT", "PATCH", "DELETE", "TRACE", "OPTIONS")
                .allowedOrigins(FRONT_END_LOCAL);
    }
}
