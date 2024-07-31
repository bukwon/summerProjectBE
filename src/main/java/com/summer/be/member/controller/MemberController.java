package com.summer.be.member.controller;

import com.summer.be.member.domain.Member;
import com.summer.be.member.domain.dto.MemberDto;
import com.summer.be.member.domain.dto.RequestLoginDto;
import com.summer.be.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "User", description = "User 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/api/user")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDto requestLoginDto) {
        Member member = memberService.login(requestLoginDto.getEmail(), requestLoginDto.getPassword());

        return ResponseEntity.ok(member.getNickname());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberDto memberDto) {
        try {
            memberService.signUp(memberDto.getEmail(), memberDto.getNickname(), memberDto.getPassword());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/check_email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        boolean emailExists = memberService.checkEmail(email);
        if (emailExists) {
            log.info("Email already exists: " + email);
            return ResponseEntity.badRequest().body("Email already exists");
        } else {
            log.info("Email is available: " + email);
            return ResponseEntity.ok().build();
        }
    }

}
