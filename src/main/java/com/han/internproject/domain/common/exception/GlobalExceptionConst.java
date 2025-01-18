package com.han.internproject.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionConst {


    // 상태코드 401
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, " 비밀번호를 확인해주세요.");


    private final HttpStatus httpStatus;
    private final String message;
}