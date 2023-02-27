package com.project.board.controller.error;

import com.project.board.type.ErrorCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler
    public String illegalStateException(IllegalStateException e, Model model){
        ErrorCode errorCode = ErrorCode.VALUE_NOT_EXIST;
        model.addAttribute(
                Map.of(
                        "errorCode",errorCode.getCode(),
                        "status", errorCode.getHttpStatus(),
                        "message", errorCode.getMessage()
                )
        );
        return "error";
    }


}
