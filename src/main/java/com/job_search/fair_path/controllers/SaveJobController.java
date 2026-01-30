package com.job_search.fair_path.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequestMapping("/saved-jobs")
public class SaveJobController {

    private final SavedJobService savedJobService;

    public SaveJobController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    @GetMapping("/list")
    public List<JobResultDTO> getSavedJobsForUser(Authentication authentication) {
        return savedJobService.getSavedJobsForUser(authentication);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveJobForUser(@RequestBody JobResultDTO jobRequestDTO,
            Authentication authentication) {
        try {
            savedJobService.saveJob(jobRequestDTO, authentication);
            return ResponseEntity.ok("success");
        } catch (Exception error) {
            return ResponseEntity.internalServerError().body("failed to save in table");
        }

    }

    @DeleteMapping("/delete")
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