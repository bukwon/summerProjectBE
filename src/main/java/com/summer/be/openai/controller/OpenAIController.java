package com.summer.be.openai.controller;


import com.summer.be.openai.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/openai")
public class OpenAIController {

    @Autowired
    private OpenAIService chatGptService;

    @GetMapping("/start")
    public String index() {
        return "openai/index";
    } // AI prompt 받을 창 메서드

    @PostMapping("/getCompletion")
    public String getCompletion(@RequestParam("prompt") String prompt, Model model) {
        String completion = chatGptService.getCompletion(prompt);

        model.addAttribute("prompt", prompt);
        model.addAttribute("completion", completion);
        return "openai/result";
    }   // 결과 값 메서드
}
