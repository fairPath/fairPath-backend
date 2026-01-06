package com.job_search.fair_path.services;

import com.job_search.fair_path.dataTransferObject.JobResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobMatchingService {
    private JobResultDTO jobResultDTO;
    @Value("${resume.job-matching.min-match-percentage}")
    private double minMatchPercentage;

    @Value("${resume.job-matching.max-results}")
    private int maxResults;

    public List<JobResultDTO> findMatchingJobs(List<String> keywords, List<String> skills) {
        log.info("Finding matching jobs for {} keywords and {} skills", keywords.size(), skills.size());
        // Combine keywords and skills for comprehensive matching
        Set<String> allSearchTerms = new HashSet<>();
        allSearchTerms.addAll(keywords);
        allSearchTerms.addAll(skills);

        //Fetch Jobs from Api using Azuna
        List<JobResultDTO> allJobs = fetchJobsFromApi(new ArrayList<>(allSearchTerms));

        // Calculate matching percentage for each job

//        List<JobResultDTO>matchedJobs = allJobs
//                .stream()
//                .map(job -> calculateMatchPercentage(job, allSearchTerms))
//                .filter(job -> job.getMachPercentage() >= minMatchPercentage)
//                .sorted(Comparator.comparingDouble(JobResultDTO::getMatchPercentage).reversed())
//                .limit(maxResults)
//                .collect(Collectors.toList());
        //return matchedJobs;
        return null;
    }

    private List<JobResultDTO> fetchJobsFromApi(List<String> searchTerms) {
        // This is a mock implementation since we don't have a real job API
        log.info("Fetching jobs from API with search terms: {}", searchTerms);
        // Logic to make api call to azuna
        try {
            return generateMockJobs(searchTerms);

        } catch (Exception e) {
            log.error("Error fetching jobs from api", e);
            // returning mock data as fall back
            return generateMockJobs(searchTerms);
        }

    }

    private JobResultDTO calculateMatchPercentage(JobResultDTO jobResultDTO, Set<String> candidateTerms) {
        // convert lowercase for case-insensitve matching
        Set<String> candidateTermsLower = candidateTerms.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // Extract keywords from job desc and required skills
        Set<String> jobTerms = new HashSet<>();
        // TO DO create getters for required skills
//        if(jobResultDTO.getRequiredSkills() != null){
//            jobTerms.addAll(jobResultDTO.getRequiredSkills().stream()
//                    .map(String::toLowerCase)
//                    .collect(Collectors.toSet()));
//        }

        // Extract terms from job description
        if (jobResultDTO.getJobDescription() != null) {
            String[] descriptionWords = jobResultDTO.getJobDescription().toUpperCase()
                    .split("[\\s,;.()]+");
            jobTerms.addAll(Arrays.asList(descriptionWords));
        }

        // Extract from job title
        if (jobResultDTO.getTitle() != null) {
            String[] titleWords = jobResultDTO.getTitle().toUpperCase().split("[\\s,;.()]+");
            jobTerms.addAll(Arrays.asList(titleWords));
        }

        // Find matching terms
        Set<String> matchedTerms = candidateTermsLower.stream()
                .filter(jobTerms::contains)
                .collect(Collectors.toSet());

        // Calculate match percentage
        double matchPercentage = 0.0;
        if (!candidateTermsLower.isEmpty()) {
            matchPercentage = (matchedTerms.size() * 100.0) / candidateTermsLower.size();
        }

        // Set match information
        // TO-DO create getters/setters for match percentage
        //jobResultDTO.setMatchPercentage(Math.round(matchPercentage * 100.0) / 100.0);
        //jobResultDTO.setMatchedKeywords(new ArrayList<>(matchedTerms));

        return jobResultDTO;


    }

    private List<JobResultDTO> generateMockJobs(List<String> searchTerms) {
        log.info("Generate mock jobs");
        List<JobResultDTO> mockJobs = new ArrayList<>();

        String[] companies = {"TechCorp", "InnoSoft", "DataSystems", "CloudTech", "AI Solutions"};
        String[] locations = {"New York, NY", "San Francisco, CA", "Austin, TX", "Seattle, WA", "Remote"};
        String[] experienceLevels = {"Entry Level", "Mid Level", "Senior Level", "Lead"};

        // Take first few terms for job title
        List<String> relevantTerms = searchTerms.stream().limit(5).collect(Collectors.toList());
        for (int i = 0; i < Math.min(15, relevantTerms.size() * 3); i++) {
            String term = relevantTerms.get(i % relevantTerms.size());
//            mockJobs.add(JobResultDTO.builder()
//                    .id("JOB" + (i + 1)))
//                    .title(generateJobTitle(term))
//                    .company(companies[i % companies.length])
//                    .location(locations[i % locations.length])
//                    .description(generateJobDescription(term, searchTerms))
//                    .requiredSkills(generateRequiredSkills(searchTerms))
//                    .experienceLevel(experienceLevels[i % experienceLevels.length])
//                    .salary("$" + (60 + i * 10) + "k - $" + (80 + i * 15) + "k")
//                    .url("https://example.com/jobs/" + (i + 1))
//                    .build());
        }
        return mockJobs;
    }

    private String generateJobTitle(String term) {
        String[] prefixes = {"Senior", "Junior", "Lead", "", "Principal"};
        String[] suffixes = {"Engineer", "Developer", "Specialist", "Analyst", "Architect"};

        return prefixes[new Random().nextInt(prefixes.length)] + " " +
                term.substring(0, 1).toUpperCase() + term.substring(1) + " " +
                suffixes[new Random().nextInt(suffixes.length)];
    }

    private String generateJobDescription(String mainTerm, List<String> allTerms) {
        return String.format("We are looking for a talented professional with experience in %s. " +
                        "The ideal candidate should have skills in %s and be passionate about technology. " +
                        "You will work with cutting-edge technologies and collaborate with a dynamic team.",
                mainTerm,
                String.join(", ", allTerms.subList(0, Math.min(3, allTerms.size()))));
    }

    private List<String> generateRequiredSkills(List<String> searchTerms) {
        return searchTerms.stream()
                .limit(5)
                .map(term -> term.substring(0, 1).toUpperCase() + term.substring(1))
                .collect(Collectors.toList());
    }
}
