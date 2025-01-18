package com.han.internproject.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private enum Status {
        SUCCESS, FAIL, ERROR
    }

    private final Status status;
    private final T data;
    private final String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(Status.SUCCESS, data, "요청이 성공적으로 처리되었습니다");
    }


    public static ApiResponse<?> error(String message) {
        return new ApiResponse<>(Status.FAIL, "", message);
    }
}
