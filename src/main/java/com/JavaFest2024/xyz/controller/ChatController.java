package com.JavaFest2024.xyz.controller;

import com.JavaFest2024.xyz.service.HuggingFaceChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private HuggingFaceChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Pattern CONTENT_FILTER = Pattern.compile("(?i)\\b(query:|answer:|program:|command:)\\b");

    @PostMapping("/api/chat")
    public ResponseEntity<ObjectNode> getChatResponse(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        logger.info("Received chat message: " + message);

        ObjectNode jsonResponse = objectMapper.createObjectNode();

        try {
            String response = chatService.getAIResponse(message);
            logger.info("Raw AI response: " + response);

            // Apply content filtering
            response = CONTENT_FILTER.matcher(response).replaceAll("");
            response = response.trim();

            if (response.isEmpty()) {
                response = "I'm sorry, I don't have a relevant answer for that question. Could you please rephrase or ask something else?";
            }

            logger.info("Filtered AI response: " + response);

            jsonResponse.put("response", response);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            logger.error("Error processing chat message", e);

            jsonResponse.put("error", "An error occurred while processing your request. Please try again later.");
            return ResponseEntity.internalServerError().body(jsonResponse);
        }
    }
}