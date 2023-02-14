package com.project.board.dto;

import com.project.board.entity.Board;
import com.project.board.type.Category;

public record BoardResponse(
        String title,
        String content,
        String tagName,
        Integer hit,
        Integer countVisit,
        Category category
) {
    public static BoardResponse of(
            String title,
            String content,
            String tagName,
            Integer hit,
            Integer countVisit,
            Category category
    ){
        return new BoardResponse(title, content, tagName, hit, countVisit, category);
    }

    public static BoardResponse fromResponse(Board board){
        return BoardResponse.of(
                board.getTitle(),
                board.getContent(),
                board.getTagName(),
                board.getHit(),
                board.getCountVisit(),
                board.getCategory()
        );
    }
}
