package com.han.internproject.domain.auth.exception;

import com.han.internproject.domain.common.exception.GlobalException;

import static com.han.internproject.domain.common.exception.GlobalExceptionConst.UNAUTHORIZED_PASSWORD;

public class UnauthorizedPasswordException extends GlobalException {
    public UnauthorizedPasswordException() {
        super(UNAUTHORIZED_PASSWORD);
    }
}
