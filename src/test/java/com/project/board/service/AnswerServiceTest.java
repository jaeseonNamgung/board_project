package com.project.board.service;

import com.project.board.dto.AnswerDto;
import com.project.board.dto.BoardDto;
import com.project.board.dto.CommentDto;
import com.project.board.dto.MemberDto;
import com.project.board.entity.Answer;
import com.project.board.entity.Board;
import com.project.board.entity.Member;
import com.project.board.exception.GeneralException;
import com.project.board.repository.AnswerRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @InjectMocks
    private AnswerService sut;
    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private MemberRepository memberRepository;

    @DisplayName("[Answer Service] 저장 - 성공 테스트")
    @Test
    void saveTest() {
        // given
        Long answerId = 3L;
        Long boardId = 2L;
        Long memberId = 1L;
        Board board = getBoard(boardId, memberId);
        Member member = getMember(memberId);
        Answer answer = getAnswer(answerId,"content",board, member);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(answerRepository.save(any())).willReturn(answer);
        // when

        Boolean expected = sut.createAnswer(getAnswerDto(memberId, boardId));

        // then
        assertThat(expected).isTrue();

        then(boardRepository).should(times(1)).findById(anyLong());
        then(memberRepository).should(times(1)).findById(anyLong());
        then(answerRepository).should(times(1)).save(any());
    }

    @DisplayName("[Answer Service] 저장 - 실패 테스트")
    @Test
    void saveTest2(){
        // given
        // when
        Throwable throwable = catchThrowable(() -> sut.createAnswer(null));

        // then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(throwable).hasMessageContaining(ErrorCode.INTERNAL_ERROR.getMessage());
    }

    @DisplayName("조회 - 최신순 (답변&댓글 한번에 조회)")
    @Test
    void searchTest(){
        // given
        Long boardId = 2L;
        Long memberId = 1L;
        List<Answer> answers = new ArrayList<>();

        for (long i = 3L; i<8L; i++){
            answers.add(getAnswer(i,"content",getBoard(boardId, memberId), getMember(memberId)));
        }
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        PageImpl<Answer> pageImpl = new PageImpl<>(answers, pageRequest, 5);
        given(answerRepository.findBoardAnswerCommentById(anyLong(), any()))
                .willReturn(pageImpl);
        // when
        Page<AnswerDto> result = sut.selectAnswer(boardId, pageRequest);
        // then
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getTotalElements()).isEqualTo(5);
        System.out.println(result.getContent());
        then(answerRepository).should(times(1)).findBoardAnswerCommentById(anyLong(), any());
    }

    @DisplayName("[Answer Service] 삭제 - 성공")
    @Test
    void deleteAnswer(){
        // given
        Long answerId = 1L;
        willDoNothing().given(answerRepository).deleteById(answerId);
        // when
        Boolean result = sut.deleteAnswer(answerId);
        // then
        assertThat(result).isTrue();
        then(answerRepository).should(times(1)).deleteById(answerId);
    }
    @DisplayName("[Answer Service] 삭제 - 실패")
    @Test
    void deleteAnswer2(){
        // given

        // when
        Throwable throwable = catchThrowable(() -> sut.deleteAnswer(null));
        // then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(throwable).hasMessageContaining(ErrorCode.INTERNAL_ERROR.getMessage());
    }

    @DisplayName("[Answer Service] 수정 - 성공")
    @Test
    void modifyAnswer(){
        // given
        Long memberId = 1L;
        Long boardId = 2L;
        Long answerId = 3L;
        Answer originalAnswer = getAnswer(answerId, "originalContent", getBoard(memberId, boardId), getMember(memberId));
        Answer changedAnswer = getAnswer(answerId, "changedContent", getBoard(memberId, boardId), getMember(memberId));
        given(answerRepository.findById(anyLong())).willReturn(
                Optional.of(originalAnswer)
        );

        // when
        Boolean result = sut.modifyAnswer(AnswerDto.fromAnswer(changedAnswer));
        // then
        assertThat(result).isTrue();
        then(answerRepository).should(times(1)).findById(anyLong());

    }
    @DisplayName("[Answer Service] 수정 - 실패")
    @Test
    void modifyTest2(){
        // given
        // when
        Throwable throwable = catchThrowable(() -> sut.modifyAnswer(null));
        // then
        assertThat(throwable).isInstanceOf(GeneralException.class);
        assertThat(throwable).hasMessageContaining(ErrorCode.VALUE_NOT_EXIST.getMessage());
    }
    private static Answer getAnswer(Long answerId,String content, Board board, Member member) {
        return Answer.builder()
                .id(answerId)
                .content(content)
                .hit(0)
                .member(member)
                .board(board)
                .build();
    }

    private static Member getMember(Long memberId) {
        return Member.builder().id(1L).email("email").password("pwd").nickname("nickname").build();
    }

    private static Board getBoard(Long boardId, Long memberId) {
        return Board.builder().id(2L).title("title").content("content").member(getMember(memberId)).build();
    }

    private static List<CommentDto> getComment(Long memberId){
        return List.of(
                CommentDto.of(
                        9L,
                "cotent",
                getMemberDto(memberId),
                null));
    }
    private static AnswerDto getAnswerDto(Long memberId, Long boardId) {
        return AnswerDto.of(
                3L,
                "content",
                0,
                getBoardDto(boardId, memberId),
                getMemberDto(memberId),
                getComment(memberId)
                );
    }

    private static MemberDto getMemberDto(Long memberId) {
        return MemberDto.of(
                memberId,"email","password","nickname"
        );
    }

    private static BoardDto getBoardDto(Long boardId, Long memberId) {
        return BoardDto.of(
                boardId,"title","content","#JPA#Spring",
                Category.QUESTION_ANSWER, 0,0,getMemberDto(memberId)
        );
    }
}