package com.job_search.fair_path.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.job_search.fair_path.entity.SavedJobsEntity;
import java.util.UUID;
import java.util.List;

@Repository
public interface SavedJobsRepository extends JpaRepository<SavedJobsEntity, UUID> {
    List<SavedJobsEntity> findByUserId(UUID userId);

    List<SavedJobsEntity> deleteByUserIdAndJobId(UUID userId, String jobId);

}
