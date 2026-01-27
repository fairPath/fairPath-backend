package com.job_search.fair_path.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import com.job_search.fair_path.services.SavedJobService;
import com.job_search.fair_path.dataTransferObject.JobResultDTO;
import com.job_search.fair_path.dataTransferObject.SaveJobRequestDTO;
import com.job_search.fair_path.entity.SavedJobsEntity;

@RestController
@CrossOrigin
@RequestMapping("/savedJobs")
public class SaveJobController {

    private final SavedJobService savedJobService;

    public SaveJobController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    @GetMapping("/list")
    public List<JobResultDTO> getSavedJobsForUser(Authentication authentication) {
        List<SavedJobsEntity> savedJobs = savedJobService.getSavedJobsForUser(authentication);
        List<JobResultDTO> jobResults = new ArrayList<>();
        savedJobs.forEach(job -> {
            JobResultDTO jobResult = new JobResultDTO(job.getJobId(), job.getJobTitle(), job.getCompanyName(),
                    job.getDateCreated(), job.getLocation(), job.getRedirectUrl(), job.getJobDescription(),
                    job.getSalaryMin(), job.getSalaryMax(), true);
            jobResults.add(jobResult);
        });
        return jobResults;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveJobForUser(@RequestBody SaveJobRequestDTO saveJobRequestDTO,
            Authentication authentication) {
        try {
            savedJobService.saveJob(saveJobRequestDTO, authentication);
            return ResponseEntity.ok("success");
        } catch (Exception error) {
            return ResponseEntity.internalServerError().body("failed to save in table");
        }

    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteJobForUser(Authentication authentication,
            @RequestParam(required = true) String jobId) {
        try {
            savedJobService.deleteSavedJob(authentication, jobId);
            return ResponseEntity.ok("successully deleted");
        } catch (Exception error) {
            return ResponseEntity.internalServerError().body("failed to delete in table");
        }
    }

}