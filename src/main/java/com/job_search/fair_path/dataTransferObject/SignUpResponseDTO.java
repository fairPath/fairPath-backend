package com.job_search.fair_path.dataTransferObject;

public class SignUpResponseDTO {
    private String message;
    private boolean success;

    public SignUpResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}