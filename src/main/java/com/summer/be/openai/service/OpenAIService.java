package com.summer.be.openai.service;


import com.summer.be.openai.dao.LearningsRepository;
import com.summer.be.openai.dao.SentencesRepository;
import com.summer.be.openai.dao.VocabularyRepository;
import com.summer.be.openai.dto.*;
import com.summer.be.openai.entity.Learnings;
import com.summer.be.openai.entity.Sentences;
import com.summer.be.openai.entity.Vocabulary;
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
    private final LearningsRepository learningsRepository;
    private final VocabularyRepository vocabularyRepository;
    private final SentencesRepository sentencesRepository;

    public String getRecommendedPhrase() {
        return getCompletion("Can you recommend a topic for daily English practice?");  // 하루에 어떤 주제를 선정할지 추천해줍니다.
    }

    public List<String> getSentencesUsingPhrase(String phrase) {
        String prompt = String.format("Generate 2 sentences using the topic '%s'.", phrase);
        String response = getCompletion(prompt);

        String[] sentences = response.split("\n");
        List<String> sentenceList = new ArrayList<>();
        Collections.addAll(sentenceList, sentences);

        return sentenceList;
    }   // 문장을 만드는 공간 (문장 스피킹 파트)

    public List<String> getVocabularyUsingPhrase(String phrase) {
        String prompt = String.format("Generate 2 vocabularies using the topic '%s'.", phrase);
        String response = getCompletion(prompt);

        String[] vocabulary = response.split("\n");
        List<String> vocaList = new ArrayList<>();
        Collections.addAll(vocaList, vocabulary);

        return vocaList;
    }   // 보카를 만드는 부분

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

    public void saveLearning(String recommendedPhrase) {
        LearningsDto learningsDto = new LearningsDto(recommendedPhrase);
        Learnings learnings = learningsDto.toEntity();
        learningsRepository.save(learnings);
    }

    public void saveVoca(List<String> voca) {
        VocabularyDto vocabularyDto = new VocabularyDto(voca);
        Vocabulary saveVoca = vocabularyDto.toEntity();
        vocabularyRepository.save(saveVoca);
    }

    public void saveSentences(List<String> sentences) {
        SentencesDto sentencesDto = new SentencesDto(sentences);
        Sentences saveSentences = sentencesDto.toEntity();
        sentencesRepository.save(saveSentences);
    }

    public List<Learnings> findOpenAI() {
        return learningsRepository.findAll();
    }
}
