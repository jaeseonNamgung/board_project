package com.project.board.dto;

import com.project.board.entity.Board;
import com.project.board.entity.Member;
import com.project.board.type.Category;

public record BoardDto(
        Long id,
        String title,
        String content,
        String tagName,
        Category category,
        Integer hit,
        Integer countVisit,
        MemberDto memberDto
) {
    public static BoardDto of(
            Long id,
            String title,
            String content,
            String tagName,
            Category category,
            Integer hit,
            Integer countVisit,
            MemberDto memberDto

    ){
        return new BoardDto(id,title, content, tagName, category, hit, countVisit,memberDto);
    }

    public Board toBoard(Member member) {
        return Board.of(
                id,
                title,
                content,
                tagName,
                hit,
                countVisit,
                category,
                member
        );
    }

}
