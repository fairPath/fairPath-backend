package com.job_search.fair_path.dataTransferObject;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ollama")
@Data
public class OllamaDTO {
    private String baseUrl;
    private String model;
    private int timeout;
}
