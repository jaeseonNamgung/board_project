package com.project.board.controller.api;

import com.project.board.dto.ApiDataResponse;
import com.project.board.dto.BoardRequest;
import com.project.board.dto.BoardResponse;
import com.project.board.service.BoardService;
import com.project.board.type.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    // 카테고리 별 조회 (최신순, 좋아요 순)
    @GetMapping("/v1/board/get/{category}")
    public ApiDataResponse<Page<BoardResponse>> getBoard(
            @PathVariable Category category,
            @PageableDefault(page = 0, size = 5, sort = "createdDate", direction = Sort.Direction.DESC)
            Pageable pageable){
        Page<BoardResponse> boardResponses = boardService.getBoardCategory(category, pageable);
        System.out.println("response = " + boardResponses);
        return ApiDataResponse.of(boardResponses);

    }

    // 검색 기능
    @GetMapping("/v1/board/search")
    public ApiDataResponse<Page<BoardResponse>> searchBoard(
            @RequestParam("searchName") String searchName,
            @PageableDefault(page = 0, size = 5, sort = "createdDate", direction = Sort.Direction.DESC)
            Pageable pageable){
        Page<BoardResponse> boardResponses = boardService.searchBoard(searchName, pageable);
        return ApiDataResponse.of(boardResponses);
    }

    // 글 작성
    @PostMapping("/v1/board/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiDataResponse<Boolean> createBoard(@RequestBody BoardRequest boardRequest, HttpSession session){
        session.getAttribute("memberId");
        return null;
    }

}
