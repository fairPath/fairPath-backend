package com.job_search.fair_path.dataTransferObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String username;
}
