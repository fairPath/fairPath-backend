package com.job_search.fair_path.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job_search.fair_path.entity.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    Optional<Resume> findByIdAndUserId(UUID id, UUID userId);

    Optional<Resume> findByUserId(UUID userId);

}
