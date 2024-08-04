package com.summer.be.openai.service;


import com.summer.be.openai.dto.Message;
import com.summer.be.openai.dto.OpenAIRequest;
import com.summer.be.openai.dto.OpenAIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OpenAIService {

    @Value("${openai.api.api-key}")
    private String openaiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";  // OpenAI 호출 URL

    public String getRecommendedPhrase() {
        return getCompletion("Can you recommend a topic for daily English practice?");
    }

    public List<String> getSentencesUsingPhrase(String phrase) {
        String prompt = String.format("Generate 10 sentences using the topic '%s'.", phrase);
        String response = getCompletion(prompt);

        String[] sentences = response.split("\n");
        List<String> sentenceList = new ArrayList<>();
        Collections.addAll(sentenceList, sentences);

        return sentenceList;
    }

    private String getCompletion(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);
        headers.set("Content-Type", "application/json");

        Message userMessage = new Message("user", prompt);
        OpenAIRequest request = new OpenAIRequest("ft:gpt-3.5-turbo-1106:personal::9sM8ENFu", Collections.singletonList(userMessage));

        try {
            HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<OpenAIResponse> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, entity, OpenAIResponse.class);

            if (response.getBody() != null && !response.getBody().getChoices().isEmpty()) {
                return response.getBody().getChoices().get(0).getMessage().getContent();
            } else {
                return "No response from OpenAI.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}
