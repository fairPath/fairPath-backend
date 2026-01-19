package com.job_search.fair_path.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JobService {

    private final RestTemplate restTemplate;

    @Value("${APP_ID}")
    private String appId;

    @Value("${APP_KEY}")
    private String appKey;

    private final String adzunaApiUrl = "https://api.adzuna.com/v1/api/jobs/us/search/0";

    public JobService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getJobs(String where, String titleOnly, Integer salaryMin, String company, String fullTime,
            String partTime,
            String contract) {
        String url = adzunaApiUrl + "?app_id=" + appId + "&app_key=" + appKey;
        if (where != null && !where.isEmpty())
            url += "&where=" + where;
        if (titleOnly != null && !titleOnly.isEmpty())
            url += "&title_only=" + titleOnly;
        if (salaryMin != null && salaryMin != 0)
            url += "&salary_min=" + salaryMin;
        if (company != null && !company.isEmpty())
            url += "&company=" + company;
        if (fullTime != null && !fullTime.isEmpty())
            url += "&full_time=" + fullTime;
        if (partTime != null && !partTime.isEmpty())
            url += "&part_time=" + partTime;
        if (contract != null && !contract.isEmpty())
            url += "&contract=" + contract;
        url += "&results_per_page=50";
        return restTemplate.getForObject(url, String.class);
    }
}
