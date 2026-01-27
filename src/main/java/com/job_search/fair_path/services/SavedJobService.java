package com.job_search.fair_path.services;

import com.job_search.fair_path.dataTransferObject.JobResultDTO;
import com.job_search.fair_path.dataTransferObject.SaveJobRequestDTO;
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

    public List<SavedJobsEntity> getSavedJobsForUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();
        List<SavedJobsEntity> savedJobs = new ArrayList<>();
        savedJobs = savedJobsRepository.findByUserId(userId);
        return savedJobs;
    }

    public void saveJob(SaveJobRequestDTO saveJobRequestDTO, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();
        String jobDesSnippet = saveJobRequestDTO.getJobDescription().substring(0, 252) + "...";
        SavedJobsEntity jobToSave = new SavedJobsEntity(saveJobRequestDTO.getJobId(), userId,
                saveJobRequestDTO.getCompanyName(), saveJobRequestDTO.getTitle(), saveJobRequestDTO.getRedirectUrl(),
                saveJobRequestDTO.getDateCreated(),
                saveJobRequestDTO.getLocation(), jobDesSnippet, saveJobRequestDTO.getSalaryMin(),
                saveJobRequestDTO.getSalaryMax());
        savedJobsRepository.save(jobToSave);
    }

    @Transactional
    public void deleteSavedJob(Authentication authentication, String jobId) {
        User user = (User) authentication.getPrincipal();
        UUID userId = user.getId();
        savedJobsRepository.deleteByUserIdAndJobId(userId, jobId);
    }

}
