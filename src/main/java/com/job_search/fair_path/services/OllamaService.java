package com.job_search.fair_path.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.job_search.fair_path.dataTransferObject.KeywordExtractionResponse;
import com.job_search.fair_path.dataTransferObject.OllamaDTO;
import com.job_search.fair_path.dataTransferObject.OllamaRequestDTO;
import com.job_search.fair_path.dataTransferObject.OllamaResponseDTO;
import com.job_search.fair_path.exception.OllamaIntegrationProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OllamaService {
    private final OllamaDTO ollamaDTO;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KeywordExtractionResponse extractKeywords(String resumeTxt) {
        log.info("Extracting key words using ollama model: {} ", ollamaDTO.getModel());
        String prompt = buildKeywordExtractionPrompt(resumeTxt);
        String response = callOllama(prompt);

        return parseKeywordResponse(response);
    }

    // generate prompt to extra keywords from resume
    private String buildKeywordExtractionPrompt(String resume) {
        return String.format("""
                Analyze the following resume and extract relevant information in JSON format.
                
                Resume Text:
                %s
                
                Please provide the analysis in the following JSON format:
                {
                  "keywords": ["keyword1", "keyword2", ...],
                  "skills": ["skill1", "skill2", ...],
                  "experience": "brief summary of work experience",
                  "education": "brief summary of education",
                  "certifications": ["cert1", "cert2", ...]
                }
                
                Guidelines:
                - Extract 10-20 most relevant keywords that represent the candidate's expertise
                - List all technical and soft skills mentioned
                - Summarize work experience in 1-2 sentences
                - Summarize education background in 1-2 sentences
                - List any certifications or licenses
                - Focus on job-relevant information
                - Provide ONLY the JSON response, no additional text
                """, resume);
    }

    // make api call to ollama using api/generate endpoint
    private String callOllama(String prompt) {
        String url = ollamaDTO.getBaseUrl() + "/api/generate";
        OllamaRequestDTO request = OllamaRequestDTO.builder()
                .model(ollamaDTO.getModel())
                .prompt(prompt)
                .stream(false)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OllamaRequestDTO> entity = new HttpEntity<>(request, headers);
        try {
            log.info("calling ollama api at: {}", url);
            ResponseEntity<OllamaResponseDTO> responseEntity = restTemplate.postForEntity(
                    url,
                    entity,
                    OllamaResponseDTO.class
            );
            if (responseEntity.getBody() == null || responseEntity.getBody().getResponse() == null) {
                throw new OllamaIntegrationProcessingException("Empty response from ollama");
            }

            String response = responseEntity.getBody().getResponse();
            log.info("Response length of characters: {}", response.length());
            return response;

        } catch (Exception e) {
            log.info("Error Calling Ollama API ", e);
            throw new OllamaIntegrationProcessingException("Failed to call Ollama API " + e.getMessage(), e);
        }

    }

    private KeywordExtractionResponse parseKeywordResponse(String response) {
        try {
            String jsonTxt = extractJsonFromTxt(response);
            JsonNode jsonNode = objectMapper.readTree(jsonTxt);
            List<String> keywords = parseJsonArray(jsonNode.get("keywords"));
            List<String> skills = parseJsonArray(jsonNode.get("skills"));
            List<String> certifications = parseJsonArray(jsonNode.get("certifications"));
            String experience = jsonNode.has("experience") ? jsonNode.get("experience").asText() : "";
            String education = jsonNode.has("education") ? jsonNode.get("education").asText() : "";

            return KeywordExtractionResponse
                    .builder()
                    .keywords(keywords)
                    .skills(skills)
                    .certifications(certifications)
                    .education(education)
                    .experience(experience)
                    .build();
        } catch (Exception e) {
            log.error("Error parsing Ollama response", e);
            log.debug("Raw response: {}", response);
            // Fall back try to extract keywords manually
            return extractKeywordsFallBack(response);

        }
    }

    private String extractJsonFromTxt(String txt) {
        int startIndx = txt.indexOf("{");
        int endIndx = txt.lastIndexOf("}");

        if (startIndx != 1 && endIndx != -1 && startIndx < endIndx) {
            return txt.substring(startIndx, endIndx + 1);
        }
        return txt;
    }

    private List<String> parseJsonArray(JsonNode arrayNode) {
        if (arrayNode == null || !arrayNode.isArray()) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        arrayNode.forEach(node -> result.add(node.asText()));
        return result;
    }

    private KeywordExtractionResponse extractKeywordsFallBack(String response) {
        log.warn("using fall back keyword extraction method");
        // Extraction based on common pattern
        List<String> keywords = Arrays.stream(response.split("[\\s,;.]+"))
                .filter(word -> word.length() > 3)
                .distinct()
                .limit(15)
                .collect(Collectors.toList());
        return KeywordExtractionResponse
                .builder()
                .keywords(keywords)
                .skills(new ArrayList<>())
                .experience("Unabled to extract structure experience")
                .education("Unabled to extract education")
                .certifications(new ArrayList<>())
                .build();
    }


}
