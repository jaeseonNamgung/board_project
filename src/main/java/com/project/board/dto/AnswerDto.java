package com.project.board.dto;

import com.project.board.entity.Answer;
import com.project.board.entity.Board;
import com.project.board.entity.Member;

import java.util.List;

public record AnswerDto(
        Long id,
        String content,
        Integer hit,
        BoardDto boardDto,
        MemberDto memberDto,
        List<CommentDto> commentDtos
) {
    public static AnswerDto of(
            Long id,
            String content,
            Integer hit,
            BoardDto boardDto,
            MemberDto memberDto,
            List<CommentDto> commentDtos
    ){
        return new AnswerDto(id, content, hit, boardDto, memberDto,commentDtos);
    }

    public Answer toAnswer() {
        return Answer.of(
                id,
                content,
                hit,
                memberDto.toMember(),
                boardDto.toBoard()
        );
    }
    public static AnswerDto fromAnswer(Answer answer){
        return AnswerDto.of(
                answer.getId(),
                answer.getContent(),
                answer.getHit(),
                BoardDto.fromBoard(answer.getBoard()),
                MemberDto.fromMember(answer.getMember()),
               CommentDto.fromCommentList(answer.getComments())
        );
    }
}
