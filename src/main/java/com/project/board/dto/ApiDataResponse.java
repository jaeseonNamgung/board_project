package com.project.board.dto;

import com.project.board.type.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApiDataResponse<T> extends ApiErrorResponse{

    private final T data;

    public ApiDataResponse(T data) {
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    public static <T> ApiDataResponse<T> of(T data){
        return new ApiDataResponse<>(data);
    }
    public static <T> ApiDataResponse<T> empty(){
        return new ApiDataResponse<>(null);
    }


}
