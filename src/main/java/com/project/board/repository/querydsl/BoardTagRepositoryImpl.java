package com.project.board.repository.querydsl;

import com.project.board.entity.BoardTag;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.project.board.entity.QBoard.*;
import static com.project.board.entity.QBoardTag.boardTag;
import static com.project.board.entity.QTag.tag;

@RequiredArgsConstructor
public class BoardTagRepositoryImpl implements BoardTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardTag> search(String word) {

        return queryFactory
                .selectFrom(boardTag).distinct()
                .join(boardTag.board, board).fetchJoin()
                .join(boardTag.tag, tag).fetchJoin()
                .where(
                        boardTag.board.title.contains(word)
                                .or(boardTag.board.content.contains(word))
                )
                .fetch();
    }


}
