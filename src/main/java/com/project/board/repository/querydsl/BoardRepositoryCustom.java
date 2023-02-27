package com.project.board.repository.querydsl;

import com.project.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> searchBoard(String word, Pageable pageable);

}
