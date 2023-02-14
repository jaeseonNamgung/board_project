package com.project.board.repository;

import com.project.board.entity.Answer;
import com.project.board.repository.querydsl.AnswerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends
        JpaRepository<Answer, Long>,
        AnswerRepositoryCustom
{

}
