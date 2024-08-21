package com.JavaFest2024.xyz.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class HuggingFaceChatService {

    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceChatService.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 5000; // 5 seconds

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAIResponse(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", message);
        requestBody.put("parameters", Map.of(
                "max_length", 150,
                "temperature", 0.7,
                "top_p", 0.9,
                "do_sample", true
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                ResponseEntity<List> response = restTemplate.postForEntity(apiUrl, request, List.class);
                List responseBody = response.getBody();
                if (responseBody != null && !responseBody.isEmpty()) {
                    Object firstResponse = responseBody.get(0);
                    if (firstResponse instanceof Map) {
                        Map<String, Object> responseMap = (Map<String, Object>) firstResponse;
                        if (responseMap.containsKey("generated_text")) {
                            String generatedText = (String) responseMap.get("generated_text");
                            return generatedText.replace(message, "").trim();
                        }
                    } else if (firstResponse instanceof String) {
                        return ((String) firstResponse).replace(message, "").trim();
                    }
                    return "Received an unexpected response format.";
                }
                return "I'm sorry, but I couldn't generate a response. Please try again.";
            } catch (HttpClientErrorException e) {
                logger.error("Error calling Hugging Face API", e);
                if (attempt < MAX_RETRIES - 1) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (Exception e) {
                logger.error("Unexpected error", e);
                return "An unexpected error occurred: " + e.getMessage();
            }
        }
        return "I'm sorry, the AI model is currently unavailable. Please try again later.";
    }
}