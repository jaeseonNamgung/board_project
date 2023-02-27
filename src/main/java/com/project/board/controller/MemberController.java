package com.project.board.controller;

import com.project.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/v1/login")
    public String login(){
        return "member/login";
    }

    @PostMapping("/v1/login/get")
    public String memberCheck(
           @Valid @RequestParam String email,
            @RequestParam String password
    ){

        return "index";
    }
}
