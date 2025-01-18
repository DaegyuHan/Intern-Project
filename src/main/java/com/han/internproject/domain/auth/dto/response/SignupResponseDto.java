package com.han.internproject.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponseDto {

    private String username;
    private String nickname;
    private List<AuthorityResponse> authoritie;

    @Getter
    public static class AuthorityResponse {
        private String authorityName;

        public AuthorityResponse(String authorityName){
            this.authorityName = authorityName;
        }
    }
}
