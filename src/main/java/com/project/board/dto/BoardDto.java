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
    public static BoardDto of(
            String title,
            String content,
            String tagName,
            Category category,
            MemberDto memberDto
    ){
        return BoardDto.of(null, title, content, tagName, category, 0, 0,memberDto);
    }

    public static BoardDto fromBoard(Board board) {
        return BoardDto.of(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getTagName(),
                board.getCategory(),
                board.getHit(),
                board.getCountVisit(),
                MemberDto.fromMember(board.getMember())
        );
    }

    public Board toBoard() {
        return Board.of(
                id,
                title,
                content,
                tagName,
                hit,
                countVisit,
                category,
                memberDto.toMember()
        );
    }

}
