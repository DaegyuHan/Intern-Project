package com.han.internproject.domain.auth.controller;

import com.han.internproject.domain.auth.dto.request.SigninRequestDto;
import com.han.internproject.domain.auth.dto.request.SignupRequestDto;
import com.han.internproject.domain.auth.dto.response.SigninResponseDto;
import com.han.internproject.domain.auth.dto.response.SignupResponseDto;
import com.han.internproject.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원가입/로그인 관리기능")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<SignupResponseDto> signup(@Valid
                                                    @RequestBody
                                                    @Parameter(description = "회원정보 입력")
                                                    SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

    // 로그인
    @PostMapping("/sign")
    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<SigninResponseDto> signin(@Valid
                                                    @RequestBody
                                                    @Parameter(description = "로그인정보 입력")
                                                    SigninRequestDto signinRequestDto) {
        return ResponseEntity.ok(authService.signin(signinRequestDto));
    }
}
