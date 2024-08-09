package com.summer.be.openai.controller;


import com.summer.be.openai.service.OpenAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "OpenAI", description = "OpenAI 관련 API 입니다.")
@Controller
@CrossOrigin
@Slf4j
@RequestMapping("/api/openai")
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    /*@GetMapping("/start")
    public String index() {
        return "openai/index";
    } */// AI prompt 받을 창 메서드

    @Operation(
            summary = "문장 생성",
            description = "오늘의 추천 주제 기반으로 문장 10개를 생성합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "문장 생성에 성공하였습니다."
    )
    @PostMapping("/saveLearnings")
    public ResponseEntity<?> saveLearnings(Model model) {
        String recommendedPhrase = openAIService.getRecommendedPhrase();
        List<String> sentences = openAIService.getSentencesUsingPhrase(recommendedPhrase);
        List<String> voca = openAIService.getVocabularyUsingPhrase(recommendedPhrase);
        log.info("my recommend phrase is " + "'" + recommendedPhrase + "'");
        try {
            openAIService.saveLearning(recommendedPhrase, sentences, voca);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
