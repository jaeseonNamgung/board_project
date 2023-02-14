package com.project.board.controller.dto;

import com.project.board.type.ErrorCode;
import lombok.Getter;

public record ApiErrorResponse(
        Boolean success,
        String errorCode,
        String message
) {

    public static ApiErrorResponse of(Boolean success, String errorCode, String message){
        return new ApiErrorResponse(success, errorCode, message);
    }

    public static ApiErrorResponse of(Boolean success, ErrorCode errorCode){
        return new ApiErrorResponse(success, errorCode.getCode(), errorCode.getMessage());
    }
    public static ApiErrorResponse of(Boolean success, ErrorCode errorCode, Exception e){
        return new ApiErrorResponse(success, errorCode.getCode(), errorCode.getMessage(e));
    }
    public static ApiErrorResponse of(Boolean success, ErrorCode errorCode, String message){
        return new ApiErrorResponse(success, errorCode.getCode(), errorCode.getMessage(message));
    }

}
