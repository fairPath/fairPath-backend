package com.job_search.fair_path.dataTransferObject;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private UUID resumeId;
    private String resumeFileName;
    private Instant resumeUpdatedAt;

}
