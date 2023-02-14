package com.project.board.repository.querydsl;

import com.project.board.entity.Answer;
import com.project.board.entity.QAnswer;
import com.project.board.entity.QComment;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.project.board.entity.QAnswer.answer;
import static com.project.board.entity.QComment.comment;

@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Answer> findBoardAnswerCommentById(Long boardId, Pageable pageable) {

        JPAQuery<Answer> contentQuery = queryFactory
                .selectFrom(answer).distinct()
                .leftJoin(answer.comments, comment).fetchJoin()
                .where(answer.board.id.eq(boardId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        if(!ObjectUtils.isEmpty(pageable)){
            for (Sort.Order order : pageable.getSort()) {
                PathBuilder<Answer> path = new PathBuilder<>(answer.getType(), answer.getMetadata());
                contentQuery.orderBy(
                        new OrderSpecifier(
                                order.getDirection().isAscending()?
                                        Order.ASC: Order.DESC,
                                path.get(order.getProperty())
                        )
                );
            }
        }
        List<Answer> content = contentQuery.fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(answer.count())
                .from(answer)
                .where(answer.board.id.eq(boardId));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
