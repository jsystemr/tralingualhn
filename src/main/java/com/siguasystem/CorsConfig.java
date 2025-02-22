package com.siguasystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig  {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api-tra/**")
                    .allowedOrigins("*") // Permite todos los orígenes (¡cambia esto en producción!)
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*");
                    registry.addMapping("/api/**")
                    .allowedOrigins("*") // Permite todos los orígenes (¡cambia esto en producción!)
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*");
                    registry.addMapping("/api-pdf/**")
                    .allowedOrigins("*") // Permite todos los orígenes (¡cambia esto en producción!)
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*");
            }
        };
    }
}