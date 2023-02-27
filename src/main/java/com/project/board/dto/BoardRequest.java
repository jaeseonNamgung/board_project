package com.project.board.dto;

import com.project.board.type.Category;

public record BoardRequest(
        String title,
        String content,
        String tagName,
        Category category
) {
    public static BoardRequest of(
            String title,
            String content,
            String tagName,
            Category category
    ){
        return new BoardRequest(title, content, tagName, category);
    }

    public BoardDto toBoardDto(MemberDto memberDto){
        return BoardDto.of(title, content, tagName, category, memberDto);
    }



}
