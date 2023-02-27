package com.project.board.dto;

import com.project.board.type.ErrorCode;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorResponse{

    private final Boolean success;
    private final String errorCode;
    private final String message;

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
