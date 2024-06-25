package com.ourpos.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
            .allowedHeaders("Set-Cookie")
            .allowedOrigins("https://m.ourpos.org")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
