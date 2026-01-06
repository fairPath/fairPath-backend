package com.job_search.fair_path.exception;

public class OllamaIntegrationProcessingException extends RuntimeException {
    public OllamaIntegrationProcessingException(String message) {
        super(message);
    }

    public OllamaIntegrationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
