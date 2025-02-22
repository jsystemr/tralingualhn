package com.siguasystem.awstextextract.controller;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import jakarta.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfig {
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        // Tamaño máximo por archivo (ej: 10MB)
        factory.setMaxFileSize(DataSize.ofMegabytes(10));

        // Tamaño máximo total de la solicitud (ej: 10MB)
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));

        return factory.createMultipartConfig();
    }

}
