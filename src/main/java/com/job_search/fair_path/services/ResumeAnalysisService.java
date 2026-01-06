package com.job_search.fair_path.services;

import com.job_search.fair_path.dataTransferObject.AnalysisResponseDTO;
import com.job_search.fair_path.dataTransferObject.JobResultDTO;
import com.job_search.fair_path.dataTransferObject.KeywordExtractionResponse;
import com.job_search.fair_path.dataTransferObject.ResumeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResumeAnalysisService {
    private final ResumeParserService resumeParserService;
    private final OllamaService ollamaService;
    private final JobMatchingService jobMatchingService;

    public AnalysisResponseDTO analyzeResumeAndFindJobs(MultipartFile file) {
        log.info("Starting resume analysis for file: {}", file.getOriginalFilename());

        try {
            // Extract text from uploaded file
            String resumeTxt = resumeParserService.extractTextFromFile(file);
            log.info("Exctracted resume text length: {} characters", resumeTxt.length());
            if (resumeTxt.trim().isEmpty()) {
                return AnalysisResponseDTO.builder()
                        .message("Failed to extract text from resume. Please try again, and ensure file not empty.")
                        .matchedJobs(new ArrayList<>())
                        .totalMatchedJobs(0)
                        .build();
            }

            // Use Ollama to extract keywords and analyze resume
            KeywordExtractionResponse extractionResponse = ollamaService.extractKeywords(resumeTxt);
            log.info("Extracted {} keywords and {} skills from resume", extractionResponse.getKeywords().size(),
                    extractionResponse.getSkills().size());

            // Build resume object
            ResumeDTO resumeDTO = ResumeDTO.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .content(truncateTxt(resumeTxt, 500))
                    .keywords(extractionResponse.getKeywords())
                    .skills(extractionResponse.getSkills())
                    .experience(extractionResponse.getExperience())
                    .education(extractionResponse.getEducation())
                    .build();

            // Find matching jobs based on keywords and skills
            List<JobResultDTO> matchedJobs = jobMatchingService.findMatchingJobs(
                    extractionResponse.getKeywords(), extractionResponse.getSkills()
            );

            log.info("Found {} matching jobs", matchedJobs.size());

            // Build and return response
            return AnalysisResponseDTO.builder()
                    .resumeAnalysis(resumeDTO)
                    .matchedJobs(matchedJobs)
                    .totalMatchedJobs(matchedJobs.size())
                    .message("Resume analyzed successfully. Found " + matchedJobs.size())
                    .build();

        } catch (Exception e) {
            log.error("Error during resume analysis", e);
            return AnalysisResponseDTO.builder()
                    .message("Error analyzing resume: " + e.getMessage())
                    .matchedJobs(new ArrayList<>())
                    .totalMatchedJobs(0)
                    .build();
        }
    }

    private String truncateTxt(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}
