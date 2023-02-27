package com.project.board.repository.querydsl;

import com.project.board.entity.BoardTag;

import java.util.List;

public interface BoardTagRepositoryCustom {
    // 검색
    List<BoardTag> search(String word);
}
