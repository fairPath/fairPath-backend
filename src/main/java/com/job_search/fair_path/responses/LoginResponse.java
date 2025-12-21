package com.job_search.fair_path.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expiresIn;

    public LoginResponse(String token, long expiresIn){
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
