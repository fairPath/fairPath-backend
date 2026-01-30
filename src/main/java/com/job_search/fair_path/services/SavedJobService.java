package com.job_search.fair_path.services;

import com.job_search.fair_path.dataTransferObject.JobResultDTO;
import com.job_search.fair_path.entity.SavedJobsEntity;
import com.job_search.fair_path.entity.User;
import com.job_search.fair_path.repository.SavedJobsRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SavedJobService {

    private final SavedJobsRepository savedJobsRepository;

    public SavedJobService(SavedJobsRepository savedJobsRepository) {
        this.savedJobsRepository = savedJobsRepository;
    }

    public List<JobResultDTO> getSavedJobsForUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();
        List<SavedJobsEntity> savedJobs = new ArrayList<>();
        savedJobs = savedJobsRepository.findByUserId(userId);
        List<JobResultDTO> jobResults = new ArrayList<>();
        savedJobs.forEach(job -> {
            JobResultDTO jobResult = new JobResultDTO(job.getJobId(), job.getJobTitle(), job.getCompanyName(),
                    job.getDateCreated(), job.getLocation(), job.getRedirectUrl(), job.getJobDescription(),
                    job.getSalaryMin(), job.getSalaryMax(), true);
            jobResults.add(jobResult);
        });
        return jobResults;
    }

    public void saveJob(JobResultDTO jobResultDTO, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();
        String jobDesSnippet = jobResultDTO.getJobDescription().substring(0, 252) + "...";
        SavedJobsEntity jobToSave = new SavedJobsEntity(jobResultDTO.getJobId(), userId,
                jobResultDTO.getCompanyName(), jobResultDTO.getTitle(), jobResultDTO.getRedirectUrl(),
                jobResultDTO.getDateCreated(),
                jobResultDTO.getLocation(), jobDesSnippet, jobResultDTO.getSalaryMin(),
                jobResultDTO.getSalaryMax());
        savedJobsRepository.save(jobToSave);
    }

    @Transactional
    public void deleteSavedJob(Authentication authentication, String jobId) {
        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();
        savedJobsRepository.deleteByUserIdAndJobId(userId, jobId);
    }

}
