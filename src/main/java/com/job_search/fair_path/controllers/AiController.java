package com.job_search.fair_path.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job_search.fair_path.services.AiService;

@RestController
@CrossOrigin
@RequestMapping("/generate")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping
    public String generate(@RequestParam(value = "prompt", defaultValue = "what is the weather today in New York") String prompt) {
        return aiService.generateResponse(prompt);
    }
}
