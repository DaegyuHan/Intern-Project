package com.han.internproject.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponseDto {

    @Schema(description = "사용자 이름", example = "DAE GYU")
    private String username;

    @Schema(description = "닉네임", example = "대규별명")
    private String nickname;

    @Schema(description = "사용자 권한", example = "ROLE_USER")
    private List<AuthorityResponse> authoritie;

    @Getter
    public static class AuthorityResponse {
        private String authorityName;

        public AuthorityResponse(String authorityName){
            this.authorityName = authorityName;
        }
    }
}
