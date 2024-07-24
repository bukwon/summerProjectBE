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

import java.util.Collections;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";  // OpenAI 호출 URL

    public String getCompletion(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);
        headers.set("Content-Type", "application/json");    // header Setting

        Message userMessage = new Message("user", prompt);
        OpenAIRequest request = new OpenAIRequest("gpt-4o-mini", Collections.singletonList(userMessage));   // body message, model 설정

        try {
            HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<OpenAIResponse> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, entity, OpenAIResponse.class);

            if (response.getBody() != null && !response.getBody().getChoices().isEmpty()) {
                return response.getBody().getChoices().get(0).getMessage().getContent(); // response 가 정상적으로 응답 했다면 내용 및 메세지 return
            } else {
                return "No response from OpenAI.";
            }   // 실패 시 에러 메시지
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
