package com.summer.be.openai.service;


import com.summer.be.openai.dao.OpenAIRepository;
import com.summer.be.openai.dto.Message;
import com.summer.be.openai.dto.OpenAIDto;
import com.summer.be.openai.dto.OpenAIRequest;
import com.summer.be.openai.dto.OpenAIResponse;
import com.summer.be.openai.entity.OpenAI;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OpenAIService {

    @Value("${openai.api.api-key}")
    private String openaiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";  // OpenAI 호출 URL
    private final OpenAIRepository openAIRepository;

    public String getRecommendedPhrase() {
        return getCompletion("Can you recommend a topic for daily English practice?");  // 하루에 어떤 주제를 선정할지 추천해줍니다.
    }

    public List<String> getSentencesUsingPhrase(String phrase) {
        String prompt = String.format("Generate 10 sentences using the topic '%s'.", phrase);   // 생성된 주제를 통해 문장 10개를 생성합니다.
        String response = getCompletion(prompt);    // openAI에 해당 프롬프트를 요청합니다.

        String[] sentences = response.split("\n");  // 받은 응답 값들을 배열에 저장
        List<String> sentenceList = new ArrayList<>();
        Collections.addAll(sentenceList, sentences);

        OpenAIDto openAIDto = new OpenAIDto(phrase, sentenceList);
        OpenAI openAI = openAIDto.toEntity();
        openAIRepository.save(openAI);

        return sentenceList;    // 10개 문장을 반환합니다.
    }

    private String getCompletion(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);
        headers.set("Content-Type", "application/json");

        Message userMessage = new Message("user", prompt);
        OpenAIRequest request = new OpenAIRequest("ft:gpt-3.5-turbo-1106:personal::9sM8ENFu", Collections.singletonList(userMessage));  // gpt-4o-mini 모델은 제한 사항이 있어 3.5-turbo 모델 기반으로 튜닝 작업 했습니다.

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
        }   // http 요청, 응답 예외처리 추가
    }
}
