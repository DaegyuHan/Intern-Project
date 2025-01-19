package com.han.internproject.domain.user.exception;

import com.han.internproject.domain.common.exception.GlobalException;

import static com.han.internproject.domain.common.exception.GlobalExceptionConst.NOT_FOUND_USER;

public class NotFoundUserException extends GlobalException {
    public NotFoundUserException() {
        super(NOT_FOUND_USER);
    }
}
