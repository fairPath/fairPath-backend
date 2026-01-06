package com.job_search.fair_path.dataTransferObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OllamaRequestDTO {
    private String model;
    private String prompt;
    private boolean stream;

    @JsonProperty("system")
    private String systemPrompt;
}
