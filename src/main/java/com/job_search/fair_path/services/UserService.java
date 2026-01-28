package com.job_search.fair_path.services;

import com.job_search.fair_path.dataTransferObject.UserProfileDTO;
import com.job_search.fair_path.entity.User;
import com.job_search.fair_path.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserProfileDTO getUserProfile(UUID userId) {
        return userRepository.getProfile(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));    }

}
