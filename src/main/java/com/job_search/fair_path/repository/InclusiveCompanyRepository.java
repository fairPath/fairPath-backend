package com.job_search.fair_path.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.job_search.fair_path.entity.InclusiveCompanyEntity;

@Repository
public interface InclusiveCompanyRepository extends JpaRepository<InclusiveCompanyEntity, String> {
}
