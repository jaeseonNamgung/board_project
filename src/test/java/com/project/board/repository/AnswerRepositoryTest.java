package com.project.board.repository;

import com.project.board.entity.Answer;
import com.project.board.entity.Board;
import com.project.board.entity.Comment;
import com.project.board.type.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @PersistenceContext
    private EntityManager em;

    @DisplayName("[JPA] 페이지 정렬(최신순)")
    @Test
    void sortPageByDateTest() throws InterruptedException {
        // given
        for(int i = 1; i <= 20; i ++){
            answerRepository.save(
                   Answer.builder()
                           .content("내용"+i)
                           .hit(i)
                           .build()

            );
            Thread.sleep(50);
        }
        // when
        PageRequest pageRequest =
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<Answer> page = answerRepository.findAll(pageRequest);
        // then
        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        List<Answer> result = page.getContent();
        assertThat(result).extracting("content")
                .containsExactly("내용20", "내용19","내용18","내용17","내용16");
        assertThat(result).extracting("hit")
                .containsExactly(20,19,18,17,16);
    }


    @DisplayName("[QueryDSL] 답변&댓글 조회 테스트 - 최신순 정렬(DESC)")
    @Test
    void boardAnswerCommentTest() throws InterruptedException {
        // given
        Board board = getBoard();
        em.persist(board);
        Answer answer1 = getAnswer("answerContent1", 1, board);
        em.persist(answer1);
        Thread.sleep(50);
        Answer answer2 = getAnswer("answerContent2", 2, board);
        em.persist(answer2);
        Thread.sleep(50);
        Answer answer3 = getAnswer("answerContent3", 3, board);
        em.persist(answer3);
        em.persist(
                    Comment.builder()
                            .content("commentContent")
                            .answer(answer1)
                            .build()
        );
            em.flush();
            em.clear();

        // when
        PageRequest pageRequest = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<Answer> paging = answerRepository.findBoardAnswerCommentById(board.getId(), pageRequest);
        // then
       assertThat(paging.getNumberOfElements()).isEqualTo(3);
       assertThat(paging.getContent()).extracting("content")
               .containsExactly("answerContent3", "answerContent2", "answerContent1");
       assertThat(paging.getContent()).extracting("hit")
               .containsExactly(3,2,1);
       assertThat(paging.getContent().get(2).getComments()).hasSize(1);
        assertThat(paging.getContent().get(2).getComments().get(0).getContent())
                .isEqualTo("commentContent");

    }
    
    @DisplayName("[QueryDSL] 답변&댓글 조회 테스트 - 조회순 정렬(DESC)")
    @Test
    void boardAnswerCommentTest2() throws InterruptedException {
        // given
        Board board = getBoard();
        em.persist(board);
        Answer answer1 = getAnswer("answerContent1", 1, board);
        em.persist(answer1);
        Thread.sleep(50);
        Answer answer2 = getAnswer("answerContent2", 2, board);
        em.persist(answer2);
        Thread.sleep(50);
        Answer answer3 = getAnswer("answerContent3", 3, board);
        em.persist(answer3);
        em.persist(
                    Comment.builder()
                            .content("commentContent")
                            .answer(answer1)
                            .build()
        );
            em.flush();
            em.clear();

        // when
        PageRequest pageRequest = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC,"hit"));
        Page<Answer> paging = answerRepository.findBoardAnswerCommentById(board.getId(), pageRequest);
        // then
       assertThat(paging.getNumberOfElements()).isEqualTo(3);
       assertThat(paging.getContent()).extracting("content")
               .containsExactly("answerContent3", "answerContent2", "answerContent1");
       assertThat(paging.getContent()).extracting("hit")
               .containsExactly(3,2,1);
       assertThat(paging.getContent().get(2).getComments()).hasSize(1);
        assertThat(paging.getContent().get(2).getComments().get(0).getContent())
                .isEqualTo("commentContent");

    }
    @DisplayName("[QueryDSL] 답변&댓글 조회 테스트 - 답변이 없을시 null 반환")
    @Test
    void boardAnswerCommentTest3(){

        // Given
        Board board = getBoard();
        em.persist(board);
        // When
        PageRequest pageRequest = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC,"hit"));
        Page<Answer> paging = answerRepository.findBoardAnswerCommentById(board.getId(), pageRequest);

        // Then
        assertThat(paging.getNumberOfElements()).isEqualTo(0);
        assertThat(paging.getContent()).isEmpty();
    }


    private static Board getBoard() {
        return Board.builder()
                .title("제목")
                .content("내용")
                .hit(2)
                .countVisit(5)
                .tagName("#Spring#JPA")
                .category(Category.QUESTION_ANSWER)
                .build();
    }

    private static Answer getAnswer(String content, int hit, Board board) {
        return  Answer.builder()
                 .content(content)
                 .hit(hit)
                 .board(board)
                 .build();
    }

}