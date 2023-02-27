package com.project.board.repository;

import com.project.board.config.JpaConfig;
import com.project.board.entity.Board;
import com.project.board.type.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaConfig.class)
@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardTagRepository boardTagRepository;


    @DisplayName("[JPA] 카테고리 조회 테스트")
    @Test
    void categorySelectTest(){
        // given
        Category category = Category.QUESTION_ANSWER;
        PageRequest pageRequest =
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        // when
        Page<Board> page = boardRepository.findByCategory(category, pageRequest);
        // then
        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.getNumberOfElements()).isEqualTo(3);
        assertThat(page.isLast()).isTrue();
        assertThat(page.isFirst()).isTrue();
        assertThat(page.getContent()).extracting("title")
                .containsExactly("파이썬 관련 오류", "자바 관련 질문", "스프링 관련 오륜");
    }

    @DisplayName("[JPA] 페이지 정렬(최신순)")
    @Test
    void sortPageByDateTest(){
        // given
        // when
        PageRequest pageRequest =
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<Board> page = boardRepository.findAll(pageRequest);
        // then
        assertThat(page.getSize()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        List<Board> result = page.getContent();
        assertThat(result).extracting("title")
                .containsExactly("나는", "집가고싶다","수업있다");
        assertThat(result).extracting("content")
                .containsExactly("운동 오류", "운동 오류","수업수업수업");
    }

    @DisplayName("[JPA] 페이지 정렬(좋아요 순)")
    @Test
    void sortPageByHitTest(){
        // given
       getBoardSorting();
        // when
        PageRequest pageRequest =
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "hit"));

        Page<Board> page = boardRepository.findAll(pageRequest);
        // then
        for (Board board : page.getContent()) {
            System.out.println("result = " + board);
        }
        System.out.println("pages = " + page.getTotalPages());
        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(6);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        List<Board> result = page.getContent();
        assertThat(result).extracting("title")
                .containsExactly("제목20", "제목19","제목18","제목17","제목16");
        assertThat(result).extracting("content")
                .containsExactly("내용20", "내용19","내용18","내용17","내용16");
        assertThat(result).extracting("hit")
                .containsExactly(20,19,18,17,16);
    }


    @DisplayName("[QueryDSL] 검색 조건 테스트 - 최신순")
    @Test
    void searchTest(){
        // given
        getBoardSorting();
        // when
        PageRequest pageRequest =
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<Board> page = boardRepository.searchBoard("제목1", pageRequest);
        // then
        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        List<Board> result = page.getContent();
        assertThat(result).extracting("title")
                .containsExactly("제목19","제목18","제목17","제목16","제목15");
        assertThat(result).extracting("content")
                .containsExactly("내용19","내용18","내용17","내용16", "내용15");
        assertThat(result).extracting("hit")
                .containsExactly(19,18,17,16,15);

    }
   
    @DisplayName("[QueryDSL] 검색 조건 테스트 - 좋아요 순")
    @Test
    void searchTest2(){
        // given
        getBoardSorting();
        // when
        PageRequest pageRequest =
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "hit"));

        Page<Board> page = boardRepository.searchBoard("제목1", pageRequest);
        // then
        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        List<Board> result = page.getContent();
        assertThat(result).extracting("title")
                .containsExactly("제목19","제목18","제목17","제목16","제목15");
        assertThat(result).extracting("content")
                .containsExactly("내용19","내용18","내용17","내용16", "내용15");
        assertThat(result).extracting("hit")
                .containsExactly(19,18,17,16,15);

    }


    private void getBoardSorting() {
        for(int i = 1; i <= 20; i ++){
            try{
                Thread.sleep(50);
            }catch (Exception e){
            }

            boardRepository.save(
                    Board.builder()
                            .title("제목"+i)
                            .content("내용"+i)
                            .tagName("#Spring#JAVA")
                            .hit(i)
                            .countVisit(i)
                            .build()
            );
        }
    }

    private static Board getBoard() {
        return Board.builder()
                .title("제목")
                .content("내용")
                .tagName("#Spring#JAVA")
                .hit(0)
                .countVisit(0)
                .build();
    }


}