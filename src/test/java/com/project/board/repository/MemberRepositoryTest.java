package com.project.board.repository;

import com.project.board.config.JpaConfig;
import com.project.board.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(JpaConfig.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

    @Test
    void test(){
        Member member = Member.builder()
                .email("a")
                .password("pwd")
                .nickname("aaa")
                .build();

        Member result = repository.save(member);

        System.out.println("result = " + result);
    }

}