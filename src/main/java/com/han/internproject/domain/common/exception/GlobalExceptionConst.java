package com.han.internproject.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionConst {


    // 상태코드 401
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, " 비밀번호를 확인해주세요."),

    // 상태코드 409
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, " 중복된 사용자 이름입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}