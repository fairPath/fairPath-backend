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
public class ResumeDTO {
    private String fileName;
    private String fileType;
    private String content;
    private List<String> keywords;
    private List<String> skills;
    private String experience;
    private String education;
}
