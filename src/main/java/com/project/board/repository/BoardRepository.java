package com.project.board.repository;

import com.project.board.entity.Board;
import com.project.board.repository.querydsl.BoardRepositoryCustom;
import com.project.board.type.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends
        JpaRepository<Board, Long>,
        BoardRepositoryCustom
{

    Page<Board> findByCategory(
            @Param("category") Category category,
            @Param("pageable") Pageable pageable);
}






