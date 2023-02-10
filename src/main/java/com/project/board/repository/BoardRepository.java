package com.project.board.repository;

import com.project.board.entity.Board;
import com.project.board.repository.querydsl.BoardRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends
        JpaRepository<Board, Long>,
        BoardRepositoryCustom
{

}






