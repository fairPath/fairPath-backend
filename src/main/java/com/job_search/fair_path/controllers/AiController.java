package com.job_search.fair_path.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.job_search.fair_path.dataTransferObject.AnswerDTO;
import com.job_search.fair_path.dataTransferObject.QuestionDTO;
import com.job_search.fair_path.services.AiService;

@RestController
@CrossOrigin
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) { this.aiService = aiService; }
    @PostMapping("/ask")
    public AnswerDTO ask(@RequestBody QuestionDTO question) {
        return aiService.askQuestion(question);
    }
    @GetMapping("/health")
    public String health() { return "RAG service is running!"; }
}
