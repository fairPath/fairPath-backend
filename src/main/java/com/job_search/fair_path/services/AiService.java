package com.job_search.fair_path.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateResponse(String prompt) {
        String response = chatClient.prompt()
                                    .user(prompt)
                                    .call()
                                    .content();
        return response;
    }
}

