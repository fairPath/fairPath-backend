package com.job_search.fair_path.services;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class ReadPdfService {
    @Value("${fairpath.ingest-on-startup:false}")
    private boolean ingestOnStartup;
    private static final Logger log = LoggerFactory.getLogger(ReadPdfService.class);
    private final VectorStore vectorStore;
    @Value("/tmp/rag-vectorstore.json")
    private String vectorStorePath;

    public ReadPdfService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() {
        if (!ingestOnStartup) {
            return;
        }
        try {
            // If a persisted store exists, load it and skip re-embedding
            File cache = new File(vectorStorePath);
            if (!ingestOnStartup) {
                return;
            }
            if (cache.exists() && cache.length() > 0) {
                log.info("Loading existing vector store from {}", vectorStorePath);
                // SimpleVectorStore.load(cache) // supported in your build
                return;
            }
            var resource = new ClassPathResource("docs/Resume.pdf");
            log.info("Reading document: {}", resource);
            var reader = new TikaDocumentReader(resource);
            List<Document> docs = reader.get();
            // Split into chunks
            var splitter = new TokenTextSplitter(); // size & overlap
            var splitDocs = splitter.apply(docs);
            // Add to the vector store (this will embed under the hood)
            vectorStore.add(splitDocs);
            // Persist to JSON so future boots are fast
            // SimpleVectorStore.save(cache)
            log.info("Successfully loaded documents into vector store");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load documents", e);
        }
    }
}
