package com.summer.be.openai.controller;


import com.summer.be.openai.dto.OpenAIDto;
import com.summer.be.openai.entity.OpenAI;
import com.summer.be.openai.service.OpenAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "OpenAI", description = "OpenAI 관련 API 입니다.")
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/openai")
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    @Operation(
            summary = "get 연습용",
            description = "only test"
    )
    @ApiResponse(
            responseCode = "200",
            description = "get에 성공하셨습니다."
    )
    @ResponseBody
    @GetMapping("/onlyTest")
    public String getTest() {
        return "test";
    }

    @Operation(
            summary = "학습 자료 가져오기",
            description = "오늘의 추천 기반으로 문장과 보카 학습 가져옵니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "학습 자료 get에 성공하셨습니다."
    )
    @GetMapping("/getLearnings")
    public List<OpenAIDto> getLearnings() {
        return openAIService.getAllOpenAI();
    }

    @Operation(
            summary = "문장 생성",
            description = "오늘의 추천 주제 기반으로 문장 10개를 생성합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "문장 생성에 성공하였습니다."
    )
    @PostMapping("/saveLearnings")
    public OpenAI saveLearnings() {
        String recommendedPhrase = openAIService.getRecommendedPhrase();
        List<String> sentences = openAIService.getSentencesUsingPhrase(recommendedPhrase);
        List<String> voca = openAIService.getVocabularyUsingPhrase(recommendedPhrase);
        log.info("my recommend phrase is " + "'" + recommendedPhrase + "'");
        OpenAI openAI = openAIService.saveLearning(recommendedPhrase, sentences, voca);
        return openAI;
    }
}