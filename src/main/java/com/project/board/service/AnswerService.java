package com.project.board.service;

import com.project.board.dto.AnswerDto;
import com.project.board.dto.BoardDto;
import com.project.board.dto.MemberDto;
import com.project.board.entity.Answer;
import com.project.board.entity.Board;
import com.project.board.entity.Member;
import com.project.board.exception.GeneralException;
import com.project.board.repository.AnswerRepository;
import com.project.board.repository.BoardRepository;
import com.project.board.repository.MemberRepository;
import com.project.board.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;


    @Transactional
    public Boolean createAnswer(AnswerDto answerDto){

        if(answerDto == null){
            throw new GeneralException(ErrorCode.INTERNAL_ERROR);
        }
        Member member = memberRepository.findById(answerDto.memberDto().id())
                .orElseThrow(() -> new GeneralException(ErrorCode.INTERNAL_ERROR, "Member id does not exist"));
        Board board = boardRepository.findById(answerDto.boardDto().id())
                .orElseThrow(() ->
                        new GeneralException(ErrorCode.INTERNAL_ERROR, "Board id does not exist"));
        MemberDto.fromMember(member);
        BoardDto.fromBoard(board);
        answerRepository.save(answerDto.toAnswer());
        return true;
    }


    public Page<AnswerDto> selectAnswer(Long boardId, Pageable pageable) {
        if(boardId == null){
            throw new GeneralException(ErrorCode.INTERNAL_ERROR);
        }
        return answerRepository.findBoardAnswerCommentById(boardId, pageable)
                .map(AnswerDto::fromAnswer);
    }

    @Transactional
    public Boolean deleteAnswer(Long answerId) {
        if(answerId == null){
            throw new GeneralException(ErrorCode.INTERNAL_ERROR);
        }
        answerRepository.deleteById(answerId);
        return true;
    }

    @Transactional
    public Boolean modifyAnswer(AnswerDto answerDto) {
        if(answerDto == null){
            throw new GeneralException(ErrorCode.VALUE_NOT_EXIST);
        }
        answerRepository.findById(answerDto.toAnswer().getId())
                .ifPresent(answer -> answer.update(answerDto));
        return true;
    }
}
