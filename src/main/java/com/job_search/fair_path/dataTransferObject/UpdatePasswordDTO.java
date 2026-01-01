package com.job_search.fair_path.dataTransferObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {

    private String resetToken;
    private String password;
}