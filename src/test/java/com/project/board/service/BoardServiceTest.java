package com.project.board.service;

import com.project.board.dto.BoardDto;
import com.project.board.dto.BoardResponse;
import com.project.board.dto.MemberDto;
import com.project.board.entity.Board;
import com.project.board.entity.Member;
import com.project.board.exception.GeneralException;
import com.project.board.repository.BoardRepository;
import com.project.board.repository.MemberRepository;
import com.project.board.type.Category;
import com.project.board.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService sut;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private MemberRepository memberRepository;

    
    @DisplayName("[Service] 게시글 저장 - 성공 테스트")
    @Test
    void savedBoardTest(){
        // given
        Long memberId = 1L;
        Member member = getMember();
        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));
        given(boardRepository.save(any(Board.class))).willReturn(getBoard());
        // when
        Boolean result = sut.createBoard(
                getBoardDto()
        );
        // then
        assertThat(result).isTrue();
        then(boardRepository).should().save(any(Board.class));
    }

    private static Member getMember() {
        return
                Member.builder().id(1L).email("email").password("password").nickname("nickname").build();
    }

    private static BoardDto getBoardDto() {
        return BoardDto.of(
                2L,
                "title",
                "content",
                "#Java#Spring",
                Category.QUESTION_ANSWER,
                0,
                0,
                MemberDto.of(
                        1L,
                        "email",
                        "password",
                        "nickname"
                ));
    }

    @DisplayName("[Service] 게시글 저장 - 실패 테스트")
    @Test
    void savedBoardTest2(){
        // given
        // when
        Boolean result = sut.createBoard(null);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("[Service] 게시글 수정 - 성공 테스트")
    @Test
    void modifiedBoardTest(){
        // Given
        given(boardRepository.findById(anyLong())).willReturn(
                Optional.ofNullable(getBoard())
        );
        BoardDto boardDto = getBoardDto();
        // When
        Boolean isTrue = sut.modifyBoard(1L,boardDto);


        // Then
        assertThat(isTrue).isTrue();
        then(boardRepository).should().findById(anyLong());
    }

    @DisplayName("[Service] 게시글 수정 - 실패 테스트 (값이 존재하지 않을 때)")
    @Test
    void modifiedBoardTest2(){
        // given
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());
        BoardDto boardDto = getBoardDto();
        // when
        Throwable thrown =
                catchThrowable(()->sut.modifyBoard(1L, boardDto));

        // then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.VALUE_NOT_EXIST.getMessage());
        then(boardRepository).should().findById(anyLong());

    }
    
    @DisplayName("[Service] 삭제 - 성공 테스트")
    @Test
    void deleteTest(){
        // given
        willDoNothing().given(boardRepository).deleteById(anyLong());
        // when
        Boolean expected = sut.deleteBoard(1L);

        // then
        assertThat(expected).isTrue();
        then(boardRepository).should(times(1)).deleteById(anyLong());
    }
    @DisplayName("[Service] 삭제 - 실패 테스트")
    @Test
    void deleteTest2(){
        // given
        // when
        Throwable thrown = catchThrowable(()->sut.deleteBoard(null));

        // then
        assertThat(thrown).isInstanceOf(GeneralException.class);
        assertThat(thrown).hasMessageContaining(ErrorCode.INTERNAL_ERROR.getMessage());

    }
    
    @DisplayName("[Service] 조회 - 성공 테스트")
    @Test
    void selectTest(){
        // given
        PageRequest pageRequest = PageRequest.of(0,5,Sort.by(Sort.Direction.DESC, "createdDate"));
        given(boardRepository.findAll(pageRequest)).willReturn(
                new PageImpl<>(
                        getBoardSorting(),pageRequest,20)
        );
        // when
        Page<BoardResponse> page = sut.selectBoard(pageRequest);

        // then
        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.getNumberOfElements()).isEqualTo(20);
        assertThat(page.getSort()).isEqualTo(Sort.by(Sort.Direction.DESC, "createdDate"));
        then(boardRepository).should().findAll(pageRequest);
    }
    
    @DisplayName("[Service] 검색 - 최신순 정렬")
    @Test
    void searchTest(){
        // given
        String word = "제";
        PageRequest pageRequest = PageRequest.of(0,5,Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Board> page = new PageImpl<>(getBoardSorting(), pageRequest, 20);
        given(boardRepository.searchBoard(anyString(), any(Pageable.class)))
                .willReturn(page);
        // when
        Page<BoardResponse> pageResponse = sut.searchBoard(word, pageRequest);

        // then
        assertThat(page.getSize()).isEqualTo(5);
        assertThat(page.getNumberOfElements()).isEqualTo(20);
        assertThat(page.hasNext()).isTrue();
        assertThat(page.getSort()).isEqualTo(Sort.by(Sort.Direction.DESC, "createdDate"));

        then(boardRepository).should().searchBoard(anyString(), any(Pageable.class));


    }
    @DisplayName("[Service] 검색 - 일치하는 값이 없을 경우 빈 값을 리턴")
    @Test
    void searchTest2(){
        // given
        given(boardRepository.searchBoard(anyString(), any(Pageable.class)))
                .willReturn(Page.empty());
        // when
        PageRequest pageRequest = PageRequest.of(0,5,Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<BoardResponse> pageResponse = sut.searchBoard("aaaa",pageRequest);

        // then
        assertThat(pageResponse).isEmpty();
        then(boardRepository).should().searchBoard(anyString(), any(Pageable.class));
    }


   

    private static Board getBoard() {
        return Board.builder()
                .title("title")
                .content("content")
                .category(Category.QUESTION_ANSWER)
                .tagName("#Spring#JPA")
                .hit(0)
                .countVisit(0)
                .build();
    }
    private List<Board> getBoardSorting() {
        List<Board> list = new ArrayList<>();
        for(int i = 1; i <= 20; i ++){
            try{
                Thread.sleep(50);
            }catch (Exception e){
            }

            list.add(Board.builder()
                    .title("제목"+i)
                    .content("내용"+i)
                    .tagName("#Spring#JAVA")
                    .hit(i)
                    .countVisit(i)
                    .build());
        }
        return list;
    }

}