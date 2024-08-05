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
    @PostMapping("/getSentences")
    public ResponseEntity<?> getSentences(Model model) {
        String recommendedPhrase = openAIService.getRecommendedPhrase();
        log.info("my recommend phrase is " + "'" + recommendedPhrase + "'");
        List<String> sentences = openAIService.getSentencesUsingPhrase(recommendedPhrase);
        return ResponseEntity.ok(sentences);
    }
}
