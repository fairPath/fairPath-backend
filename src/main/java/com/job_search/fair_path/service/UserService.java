package com.job_search.fair_path.service;

import com.job_search.fair_path.entity.User;
import com.job_search.fair_path.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    // Class to allow users to access the endpoints and everything else
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
    }

    // Returns a list of all users as objects and add them to our list
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
}
