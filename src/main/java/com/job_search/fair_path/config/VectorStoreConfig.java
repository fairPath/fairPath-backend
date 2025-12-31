package com.job_search.fair_path.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class VectorStoreConfig {
    private final EmbeddingModel embeddingModel;
    public VectorStoreConfig(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }
    @Bean
    public VectorStore vectorStore() {
        // In-memory store with optional JSON persistence
        return SimpleVectorStore.builder(embeddingModel)
                // .path(vectorStorePath) // persist/load to this file (enabled in your build)
                .build();
    }
}
