package com.job_search.fair_path.controllers;

import com.job_search.fair_path.dataTransferObject.AnalysisResponseDTO;
import com.job_search.fair_path.services.ResumeAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("/api/resume")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ResumeAnalyzerController {
    private final ResumeAnalysisService resumeAnalysisService;

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnalysisResponseDTO> analyzeResume(@RequestParam("file") MultipartFile file) {
        log.info("Received resume upload request: {}", file.getOriginalFilename());

        // Validate file
        if (file.isEmpty()) {
            AnalysisResponseDTO errResponse = AnalysisResponseDTO.builder()
                    .message("File is empty. Please upload a valid resume")
                    .totalMatchedJobs(0)
                    .build();
            return ResponseEntity.badRequest().body(errResponse);
        }
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !isValidFileType(contentType)) {
            AnalysisResponseDTO errorResponse = AnalysisResponseDTO.builder()
                    .message("Invalid file type. Support file is PDF.")
                    .totalMatchedJobs(0)
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            AnalysisResponseDTO response = resumeAnalysisService.analyzeResumeAndFindJobs(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing resume", e);
            AnalysisResponseDTO errorResponse = AnalysisResponseDTO.builder()
                    .message("Internal server error: " + e.getMessage())
                    .totalMatchedJobs(0)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private boolean isValidFileType(String contentType) {
        return contentType.equals("application/pdf");
    }
}
