package com.workstudy.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String prodUrl = System.getenv("FRONTEND_URL");
        if (prodUrl == null) prodUrl = "http://localhost:5173";
        
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:5174", prodUrl)
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
