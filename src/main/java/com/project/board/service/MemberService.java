package com.project.board.service;

import com.project.board.dto.MemberDto;
import com.project.board.entity.Member;
import com.project.board.exception.GeneralException;
import com.project.board.repository.MemberRepository;
import com.project.board.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Boolean createMember(MemberDto memberDto){
        if(memberDto == null){
            throw new GeneralException(ErrorCode.INTERNAL_ERROR);
        }

        memberRepository.save(memberDto.toMember());

        return true;
    }

    public MemberDto findMember(Long memberId){
        if(memberId == null){
            throw new GeneralException(ErrorCode.INTERNAL_ERROR);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new GeneralException(ErrorCode.INTERNAL_ERROR));
        return MemberDto.fromMember(member);
    }
}
