package com.job_search.fair_path.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.job_search.fair_path.dataTransferObject.AnswerDTO;
import com.job_search.fair_path.dataTransferObject.QuestionDTO;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    public AiService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }
    public AnswerDTO askQuestion(QuestionDTO question) {
        var response = chatClient
            .prompt()
            // The advisor performs vector search and injects the top chunks as context
            .advisors(new QuestionAnswerAdvisor(vectorStore))
            .user(question.question())
            .call()
            .chatResponse();
        var text = response != null ? response.getResult().getOutput().getText()
                                    : "Sorry, I couldn't find an answer.";
        return new AnswerDTO(text);
    }
}


