package com.summer.be.openai.controller;


import com.summer.be.openai.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/openai")
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;

    @GetMapping("/start")
    public String index() {
        return "openai/index";
    } // AI prompt 받을 창 메서드

    @PostMapping("/getSentences")
    public String getSentences(Model model) {
        String recommendedPhrase = openAIService.getRecommendedPhrase();
        log.info("my recommend phrase is " + "'" + recommendedPhrase + "'");
        model.addAttribute("recommendedPhrase", recommendedPhrase);
        List<String> sentences = openAIService.getSentencesUsingPhrase(recommendedPhrase);
        model.addAttribute("phrase", recommendedPhrase);
        model.addAttribute("sentences", sentences);
        return "sentences";
    }
}
