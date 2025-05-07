package com.job_search.fair_path.controllers;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JobService {

    private final RestTemplate restTemplate;

    private final String adzunaApiUrl = "https://api.adzuna.com/v1/api/jobs/us/search/1?app_id=7d4f3da8&app_key=f35c5cdc4b2b9b5b65548f119d838916&results_per_page=120";

    public JobService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getJobs(String where, String titleOnly, Integer salaryMin, String fullTime, String partTime) {
        String url = adzunaApiUrl;
        if (where != null && !where.isEmpty())
            url += "&where=" + where;
        if (titleOnly != null && !titleOnly.isEmpty())
            url += "&title_only=" + titleOnly;
        if (salaryMin != null && salaryMin != 0)
            url += "&salary_min=" + salaryMin;
        if (fullTime != null && !fullTime.isEmpty())
            url += "&full_time=" + fullTime;
        if (partTime != null && !partTime.isEmpty())
            url += "&part_time=" + partTime;
        return restTemplate.getForObject(url, String.class);
    }
}
