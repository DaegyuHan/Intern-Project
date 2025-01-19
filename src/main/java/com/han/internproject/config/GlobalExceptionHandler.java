package com.han.internproject.config;

import com.han.internproject.domain.common.exception.GlobalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public ResponseEntity<String> handleGlobalException(GlobalException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
}