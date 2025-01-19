package com.han.internproject.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequestDto {

    @NotBlank
    @Schema(description = "사용자 이름", example = "DAE GYU")
    private String username;

    @NotBlank
    @Schema(description = "비밀번호", example = "12341234")
    private String password;
}
