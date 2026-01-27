package com.job_search.fair_path.dataTransferObject;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveJobRequestDTO extends JobResultDTO {
    private UUID userId;

    public SaveJobRequestDTO(UUID userId, String jobId, String title, String companyName, String dateCreated,
            String location,
            String redirectUrl,
            String jobDescription, Double salaryMin, Double salaryMax) {
        super(jobId, title, companyName, dateCreated, location, redirectUrl,
                jobDescription, salaryMin, salaryMax, true);
        this.userId = userId;
    }
}
