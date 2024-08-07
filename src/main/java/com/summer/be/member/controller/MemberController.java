package com.summer.be.member.controller;

import com.summer.be.member.domain.Member;
import com.summer.be.member.domain.dto.KakaoAccountIdDto;
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
            summary = "로그인",
            description = "카카오 계정 아이디로 로그인을 합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인에 성공하였습니다."
    )

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody KakaoAccountIdDto kakaoAccountIdDto) {
        Member member = memberService.login(kakaoAccountIdDto.getKakaoAccountId());

        return ResponseEntity.ok(member.getLevel());
    }

    @Operation(
            summary = "회원가입",
            description = "카카오 계정 아이디로 회원가입을 합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원가입에 성공하였습니다."
    )

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody KakaoAccountIdDto kakaoAccountIdDto) {
        try {
            Member member = memberService.signUp(kakaoAccountIdDto.getKakaoAccountId());
            return ResponseEntity.ok(member.getLevel());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}