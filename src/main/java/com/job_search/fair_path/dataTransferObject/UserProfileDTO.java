package com.job_search.fair_path.dataTransferObject;

import java.util.UUID;

import com.job_search.fair_path.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public static UserProfileDTO from(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
