package com.project.board.controller;

import com.project.board.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BoardController.class)
class BoardControllerTest {

    private final MockMvc mvc;
    @MockBean
    private BoardService boardService;
    public BoardControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("모든 게시판 조회 테스트")
    @Test
    void listTest() throws Exception {
        // Given

        given(boardService.selectBoard(any())).willReturn(Page.empty());
        // When & Then
        mvc.perform(get("/v1/board/select")
                        .contentType(MediaType.TEXT_HTML_VALUE)
                        .queryParam("pageable",
                                String.valueOf(PageRequest.of(0,5,
                                        Sort.by(Sort.Direction.DESC,"createdBy")))
                ))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(view().name("board/list"))
                .andExpect(model().attributeExists("board"));
        then(boardService).should().selectBoard(any());
    }
}