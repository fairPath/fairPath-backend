package com.job_search.fair_path.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OllamaResponseDTO {
    private String model;
    private String response;
    private boolean done;
    private long totalDuration;
}
