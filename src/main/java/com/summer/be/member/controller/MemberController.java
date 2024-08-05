package com.summer.be.member.controller;

import com.summer.be.member.domain.Member;
import com.summer.be.member.domain.dto.CheckEmailDto;
import com.summer.be.member.domain.dto.MemberDto;
import com.summer.be.member.domain.dto.RequestLoginDto;
import com.summer.be.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/api/user")
public class MemberController {
    private final MemberService memberService;

    @Operation(
            summary = "일반 로그인",
            description = "로그인을 합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인에 성공하였습니다."
    )

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDto requestLoginDto) {
        Member member = memberService.login(requestLoginDto.getEmail(), requestLoginDto.getPassword());

        return ResponseEntity.ok(member.getNickname());
    }

    @Operation(
            summary = "일반 회원가입",
            description = "회원가입을 합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원가입에 성공하였습니다."
    )

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberDto memberDto) {
        try {
            memberService.signUp(memberDto.getEmail(), memberDto.getNickname(), memberDto.getPassword());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "이메일 중복 검사",
            description = "이메일 유효성 검사를 합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "이메일이 중복되지 않습니다."
    )

    @PostMapping("/check_email")
    public ResponseEntity<?> checkEmail(@RequestBody CheckEmailDto checkEmailDto) {
        String email = checkEmailDto.getEmail();
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