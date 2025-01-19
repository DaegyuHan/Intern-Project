package com.han.internproject.domain.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health 확인 및 Login 유지 확인 기능")
public class CommonController {
    // health check
    @GetMapping("/health")
    @Operation(summary = "헬스 체크")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "정상")
            ))
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("정상");
    }

    // token check
    @GetMapping("/check/token")
    @Operation(summary = "로그인 유지 확인")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "로그인 유지 중")
            ))
    public ResponseEntity<String> checkToken() {
        return ResponseEntity.ok("로그인 유지 중");
    }
}
