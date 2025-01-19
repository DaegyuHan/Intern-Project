package com.han.internproject.domain.auth.controller;

import com.han.internproject.domain.auth.dto.request.SigninRequestDto;
import com.han.internproject.domain.auth.dto.request.SignupRequestDto;
import com.han.internproject.domain.auth.dto.response.SigninResponseDto;
import com.han.internproject.domain.auth.dto.response.SignupResponseDto;
import com.han.internproject.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

    // 로그인
    @PostMapping("/sign")
    public ResponseEntity<SigninResponseDto> signin(@Valid @RequestBody SigninRequestDto signinRequestDto) {
        return ResponseEntity.ok(authService.signin(signinRequestDto));
    }

    // health check
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("정상");
    }

    // token check
    @GetMapping("/check/token")
    public ResponseEntity<String> checkToken() {
        return ResponseEntity.ok("로그인 유지 중");
    }
}
