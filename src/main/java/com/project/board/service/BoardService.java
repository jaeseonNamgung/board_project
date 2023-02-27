package com.project.board.service;

import com.project.board.dto.BoardDto;
import com.project.board.dto.BoardRequest;
import com.project.board.dto.BoardResponse;
import com.project.board.dto.MemberDto;
import com.project.board.entity.Board;
import com.project.board.entity.Member;
import com.project.board.exception.GeneralException;
import com.project.board.repository.BoardRepository;
import com.project.board.repository.MemberRepository;
import com.project.board.type.Category;
import com.project.board.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 저장
    @Transactional
    public Boolean createBoard(BoardDto boardDto) {

        if(boardDto == null){
            return false;
        }

        MemberDto memberDto = memberRepository.findById(boardDto.memberDto().id())
                            .map(MemberDto::fromMember)
                            .orElseThrow(()->new GeneralException(ErrorCode.INTERNAL_ERROR));
        boardRepository.save(boardDto.toBoard());
        return true;
    }


    @Transactional
    public Boolean modifyBoard(Long boardId, BoardDto boardDto) {

        if(boardId == null || boardDto == null){
            return false;
        }
               Board board =  boardRepository.findById(boardId)
                        .orElseThrow(()->new GeneralException(ErrorCode.VALUE_NOT_EXIST));
        board.update(boardDto);
        return true;
    }

    @Transactional
    public Boolean deleteBoard(Long boardId) {
        if(boardId == null){
            throw new GeneralException(ErrorCode.INTERNAL_ERROR);
        }
        boardRepository.deleteById(boardId);
        return true;
    }

    public Page<BoardResponse> selectBoard(Pageable pageable){
        Page<Board> page = boardRepository.findAll(pageable);
        return page.map(BoardResponse::fromResponse);
    }

    public Page<BoardResponse> searchBoard(String word, Pageable pageable) {
        if(!StringUtils.hasText(word)){
            return boardRepository.findAll(pageable).map(BoardResponse::fromResponse);
        }
        return boardRepository.searchBoard(word, pageable).map(BoardResponse::fromResponse);
    }

    public Page<BoardResponse> getBoardCategory(Category category, Pageable pageable) {
        if(category == null || pageable == null){
            throw new GeneralException(ErrorCode.INTERNAL_ERROR);
        }
       return boardRepository.findByCategory(category, pageable).map(BoardResponse::fromResponse);
    }
}
