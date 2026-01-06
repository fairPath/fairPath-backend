package com.job_search.fair_path.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponseDTO {
    private ResumeDTO resumeAnalysis;
    private List<JobResultDTO> matchedJobs;
    private int totalMatchedJobs;
    private String message;
}
