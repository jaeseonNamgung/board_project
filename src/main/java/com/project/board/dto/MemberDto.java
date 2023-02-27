package com.project.board.dto;

import com.project.board.entity.Member;

public record MemberDto(
        Long id,
        String email,
        String password,
        String nickname
){
    public static MemberDto of(
            Long id,
            String email,
            String password,
            String nickname
    ){
        return new MemberDto(id,email, password, nickname);
    }

    public static MemberDto fromMember(Member member) {
        return MemberDto.of(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getNickname()
        );
    }

    public Member toMember() {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
