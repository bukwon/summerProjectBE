package com.summer.be.member.controller;

import com.summer.be.member.domain.dto.KakaoDto;
import com.summer.be.member.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class KakaoController {
    private final KakaoService kakaoService;
    @RequestMapping("/login/oauth2/code/kakao")
    public String kakaoLogin(@RequestParam("code") String code) throws IOException {
        // 1. 인가 코드 받기 (@RequestParam String code)

        // 2. 토큰 받기
        String accessToken = kakaoService.getAccessToken(code);

        // 3. 사용자 정보 받기
        KakaoDto userInfo = kakaoService.getUserInfo(accessToken);

        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();

        System.out.println("email = " + email);
        System.out.println("nickname = " + nickname);
        System.out.println("accessToken = " + accessToken);

        return "redirect:/result";
    }

    @GetMapping("/kakaologin")
    public String loginForm(Model model){
        /*kakaoService.kakaoMethod();
        System.out.println(kakaoService.getKakaoApiKey());
        System.out.println(kakaoService.getKakaoRedirectUri());
        model.addAttribute("kakaoApiKey", kakaoService.getKakaoApiKey());
        model.addAttribute("redirectUri", kakaoService.getKakaoRedirectUri());*/
        return "login";
    }
}
