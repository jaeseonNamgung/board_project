package com.project.board.repository.querydsl;

import com.project.board.entity.Board;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.project.board.entity.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Board> searchBoard(String word, Pageable pageable) {
        JPAQuery<Board> query = queryFactory
                .selectFrom(board)
                .where(
                        board.title.contains(word)
                                .or(board.content.contains(word))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(!ObjectUtils.isEmpty(pageable)){
            for(Sort.Order order: pageable.getSort()){
                PathBuilder<Board> pathBuilder = new PathBuilder<>(board.getType(), board.getMetadata());
                query.orderBy(
                        new OrderSpecifier(
                                order.getDirection().isAscending()?
                                        Order.ASC:Order.DESC,
                                pathBuilder.get(order.getProperty())));
            }
        }
        List<Board> content = query.fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.title.contains(word)
                                .or(board.content.contains(word)));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
}
}
