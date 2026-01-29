package com.job_search.fair_path.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "saved_jobs")
@Getter
@Setter
public class SavedJobsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Column(name = "job_id", nullable = false)
    private String jobId;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "job_title")
    private String jobTitle;
    @Column(name = "redirect_url")
    private String redirectUrl;
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @Column(name = "location")
    private String location;
    @Column(name = "job_description")
    private String jobDescription;
    @Column(name = "salary_min")
    private Double salaryMin;
    @Column(name = "salary_max")
    private Double salaryMax;

    public SavedJobsEntity(String jobId, UUID userId, String companyName, String jobTitle, String redirectUrl,
            LocalDateTime dateCreated, String location,
            String jobDescription, Double salaryMin, Double salaryMax) {
        this.companyName = companyName;
        this.userId = userId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.dateCreated = dateCreated;
        this.location = location;
        this.redirectUrl = redirectUrl;
        this.jobDescription = jobDescription;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
    }

    public SavedJobsEntity() {
    }
}
