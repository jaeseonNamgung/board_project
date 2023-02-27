package com.project.board.repository;

import com.project.board.entity.BoardTag;
import com.project.board.repository.querydsl.BoardTagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTagRepository extends
        JpaRepository<BoardTag, Long>,
        BoardTagRepositoryCustom
{
}
