package com.project.board.repository.querydsl;

import com.project.board.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnswerRepositoryCustom {
    Page<Answer> findBoardAnswerCommentById(Long boardId, Pageable pageable);
}
