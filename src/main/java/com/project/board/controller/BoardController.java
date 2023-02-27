package com.project.board.controller;

import com.project.board.dto.BoardResponse;
import com.project.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/v1/board/select")
    public String select(
            Model model,
            @PageableDefault(page = 0, size = 5, sort = "createdDate",direction = Sort.Direction.DESC)
            Pageable pageable){
        Page<BoardResponse> boardResponses = boardService.selectBoard(pageable);
        model.addAttribute("board", boardResponses);
        return "board/list";
    }
}
