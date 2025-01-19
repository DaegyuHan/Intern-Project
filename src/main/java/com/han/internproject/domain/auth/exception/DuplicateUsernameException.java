package com.han.internproject.domain.auth.exception;

import com.han.internproject.domain.common.exception.GlobalException;

import static com.han.internproject.domain.common.exception.GlobalExceptionConst.DUPLICATE_USERNAME;

public class DuplicateUsernameException extends GlobalException {
    public DuplicateUsernameException() {
        super(DUPLICATE_USERNAME);
    }
}
