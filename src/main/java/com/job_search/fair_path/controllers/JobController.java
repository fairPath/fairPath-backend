package com.job_search.fair_path.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job_search.fair_path.dataTransferObject.JobResultDTO;
import com.job_search.fair_path.services.JobService;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<JobResultDTO> getJobs(@RequestParam(required = false) String where,
            @RequestParam(required = false) String titleOnly, @RequestParam(required = false) Integer salaryMin,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String fullTime, @RequestParam(required = false) String partTime,
            @RequestParam(required = false) String contract, Authentication authentication) {

        return jobService.getJobs(where, titleOnly, salaryMin, company, fullTime, partTime, contract, authentication);
    }

}