package com.job_search.fair_path.repository;

import com.job_search.fair_path.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    // Optional is used a not guaranteed return of a user
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);
}
