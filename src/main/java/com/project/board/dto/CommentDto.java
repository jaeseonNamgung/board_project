package com.project.board.dto;

import com.project.board.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

public record CommentDto(
        Long id,
        String content,
        MemberDto memberDto,
        AnswerDto answerDto
) {

    public static CommentDto of(
            Long id,
            String content,
            MemberDto memberDto,
            AnswerDto answerDto
    ){
        return new CommentDto(id, content, memberDto, answerDto);
    }

    public Comment toComment(){
        return Comment.builder()
                .id(id)
                .content(content)
                .member(memberDto.toMember())
                .answer(answerDto.toAnswer())
                .build();
    }

    public static List<CommentDto> fromCommentList(List<Comment> comments) {
        return comments.stream().map(
                CommentDto::fromComment
        ).collect(Collectors.toList());
    }

    private static CommentDto fromComment(Comment comment) {
        return CommentDto.of(
                comment.getId(),
                comment.getContent(),
                MemberDto.fromMember(comment.getMember()),
                AnswerDto.fromAnswer(comment.getAnswer())
        );
    }
}
