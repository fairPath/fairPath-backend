package com.job_search.fair_path.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.job_search.fair_path.dataTransferObject.JobResultDTO;
import com.job_search.fair_path.services.JobService;

@RestController
@CrossOrigin
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<JobResultDTO> getJobs(@RequestParam(required = false) String where,
            @RequestParam(required = false) String titleOnly, @RequestParam(required = false) Integer salaryMin,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String fullTime, @RequestParam(required = false) String partTime,
            @RequestParam(required = false) String contract) {
        List<JobResultDTO> jobs = new ArrayList<>();
        List<String> ratingList = new ArrayList<>();
        Integer totalCountOfJobs = 0;
        String apiResponse = jobService.getJobs(where, titleOnly, salaryMin, company, fullTime, partTime, contract);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(apiResponse);
            totalCountOfJobs = rootNode.get("count").asInt();
            System.out.println("********************" + totalCountOfJobs);
            ArrayNode arrayNode = (ArrayNode) rootNode.get("results");

            if (arrayNode != null) {
                for (JsonNode node : arrayNode) {
                    String title = node.get("title").asText();
                    String companyName = node.path("company").path("display_name").asText();
                    String dateCreated = node.get("created").asText();
                    String locationDisplayName = node.path("location").path("display_name").asText();
                    String locationCountry = node.get("location").get("area").get(0).asText();
                    String location = new StringBuilder(
                            locationDisplayName + ", " + locationCountry).toString();
                    String redirectUrl = node.get("redirect_url").asText();
                    String jobDescription = node.get("description").asText();
                    Double salary_min = node.get("salary_min").asDouble();
                    Double salary_max = node.get("salary_max").asDouble();

                    JobResultDTO job = new JobResultDTO(title, companyName, dateCreated, location, redirectUrl,
                            jobDescription, salary_min, salary_max);
                    System.out.println(job);

                    jobs.add(job);
                }
            }
            System.out.println("rating list==========================" + ratingList.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobs;
    }

}