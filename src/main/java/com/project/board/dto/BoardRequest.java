package com.project.board.dto;

import com.project.board.type.Category;

public record BoardRequest(
        String title,
        String content,
        String tagName,
        Category category,
        MemberDto memberDto
) {
    public static BoardRequest of(
            String title,
            String content,
            String tagName,
            Category category,
            MemberDto memberDto
    ){
        return new BoardRequest(title, content, tagName, category, memberDto);
    }




}
