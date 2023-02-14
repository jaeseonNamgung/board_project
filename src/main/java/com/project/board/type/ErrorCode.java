package com.project.board.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.BooleanNode;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    OK("OK", HttpStatus.OK, "success"),
    BAD_REQUEST("c100", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    SPRING_BAD_REQUEST("c101", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND("c102", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다"),
    VALIDATION_ERROR("c103", HttpStatus.BAD_REQUEST, "요청에 대한 validation 에러입니다."),
    USER_ACCESS_ERROR("c104", HttpStatus.BAD_REQUEST, "사용자의 접근이 허용되지 않습니다."),
    INTERNAL_ERROR("c200", HttpStatus.INTERNAL_SERVER_ERROR, "일시적인 장애입니다."),
    SPRING_INTERNAL_ERROR("c201", HttpStatus.INTERNAL_SERVER_ERROR, "일시적인 장애입니다."),
    DATA_ACCESS_ERROR("c202", HttpStatus.INTERNAL_SERVER_ERROR, "데이터 액세스 오류입니다."),

    // Member
    // 아이디 또는 비밀번호를 찾을 수 없을 때
    // 로그인이 실패 했을 때
    NOT_FOUND_ID_OR_PASSWORD("M100", HttpStatus.BAD_REQUEST,"아이디 또는 비밀번호가 존재하지 않습니다."),
    LOGIN_FAILED9("M101", HttpStatus.BAD_REQUEST, "로그인 실패입니다."),

    // Board, Answer, Comment
    WRITING_ERROR("B100", HttpStatus.BAD_REQUEST, "글쓰기 오류입니다."),
    NOT_SAVED("B101", HttpStatus.BAD_REQUEST, "저장되지 않았습니다."),
    VALUE_NOT_EXIST("B102", HttpStatus.BAD_REQUEST, "값이 존재하지 않습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e){
        return getMessage(e.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message){
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public ErrorCode isClientError(){return ErrorCode.BAD_REQUEST;}
    public ErrorCode isServerError(){return ErrorCode.INTERNAL_ERROR;}

}
