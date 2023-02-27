package com.project.board.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.board.dto.BoardRequest;
import com.project.board.dto.BoardResponse;
import com.project.board.dto.MemberDto;
import com.project.board.service.BoardService;
import com.project.board.type.Category;
import com.project.board.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(BoardApiController.class)
class BoardApiControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;
    @MockBean
    private BoardService boardService;

    public BoardApiControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("[API] 카테고리 조회 테스트")
    @Test
    void getBoardCategoryTest() throws Exception {
        // given
        PageRequest pageRequest =
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        BoardResponse boardResponse1 = getBoardResponse("스프링 관련 오륜", "스프링 오류", "#Spring#Jave", Category.QUESTION_ANSWER, 0, 2);
        BoardResponse boardResponse2 = getBoardResponse("자바 관련 질문", "자바 질문 있습니다", "#Spring#Jave", Category.QUESTION_ANSWER, 0, 1);
        BoardResponse boardResponse3 = getBoardResponse("파이썬 관련 오류", "파이썬 오류", "#Spring#Jave", Category.QUESTION_ANSWER, 0, 3);
        given(boardService.getBoardCategory(Category.QUESTION_ANSWER, pageRequest))
                .willReturn(
                        new PageImpl<>(List.of(boardResponse3, boardResponse2, boardResponse1))
                );
        // when
        mvc.perform(
                get("/v1/board/get/" + "QUESTION_ANSWER")
                        .queryParam("pageable", String.valueOf(pageRequest))
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].title").value("파이썬 관련 오류"))
                .andExpect(jsonPath("$.data.content[0].content").value("파이썬 오류"))
                .andExpect(jsonPath("$.data.content[0].category").value("QUESTION_ANSWER"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
        // then
        then(boardService).should().getBoardCategory(Category.QUESTION_ANSWER, pageRequest);
    }

    @DisplayName("[API] 검색 엔진 기능 테스트")
    @Test
    void searchBoardTest() throws Exception {
        // given
        PageRequest pageRequest =
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        BoardResponse boardResponse1 = getBoardResponse("스프링 관련 오륜", "스프링 오류", "#Spring#Jave", Category.QUESTION_ANSWER, 0, 2);
        BoardResponse boardResponse2 = getBoardResponse("자바 관련 질문", "자바 질문 있습니다", "#Spring#Jave", Category.QUESTION_ANSWER, 0, 1);
        BoardResponse boardResponse3 = getBoardResponse("파이썬 관련 오류", "파이썬 오류", "#Spring#Jave", Category.QUESTION_ANSWER, 0, 3);
        given(boardService.searchBoard(any(String.class), any(Pageable.class)))
                .willReturn(
                        new PageImpl<>(List.of(boardResponse3))
                );
        // when & then
        mvc.perform(
                get("/v1/board/search")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("searchName", "오류")
                        .queryParam("pageable",String.valueOf(pageRequest))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data.content[0].title").value("파이썬 관련 오류"))
                .andExpect(jsonPath("$.data.content[0].content").value("파이썬 오류"))
                .andExpect(jsonPath("$.data.content[0].category").value("QUESTION_ANSWER"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));

        then(boardService).should(times(1)).searchBoard(any(String.class), any(Pageable.class));
    }
    
    @DisplayName("[API] 게시판 추가 테스트")
    @Test
    void createBoardTest() throws Exception {
        // Given
        MemberDto memberDto = MemberDto.of(99L, "email", "1234", "홍길동");
        BoardRequest boardRequest = BoardRequest.of(
                "새 글", "새 글입니다", "#새글#좋아요",Category.QUESTION_ANSWER);
        given(boardService.createBoard(boardRequest.toBoardDto(memberDto)))
                .willReturn(true);

        // When & Then
        mvc.perform(
                post("/v1/board/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(boardRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.data").value(true))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

    private BoardResponse getBoardResponse(String title, String content, String tagName, Category category, int hit, int countVisit) {
            return BoardResponse.of(title, content, tagName, hit, countVisit, category);
    }
    
    

}