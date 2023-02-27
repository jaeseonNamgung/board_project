package com.project.board.entity;

import com.project.board.dto.AnswerDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"content", "hit"})
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;
    private String content;
    private Integer hit;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    @OneToMany(mappedBy = "answer")
    private List<Comment> comments = new ArrayList<>();


    @CreatedDate
    LocalDateTime createdDate;
    @LastModifiedDate
    LocalDateTime updatedDate;

    @Builder
    public Answer(Long id, String content, Integer hit, Member member, Board board) {
        this.id = id;
        this.content = content;
        this.hit = hit;
        this.member = member;
        this.board = board;
    }

    public static Answer of(Long id, String content, Integer hit, Member member, Board board){
        return new Answer(id, content, hit, member, board);
    }

    public Answer update(AnswerDto answerDto) {
        return Answer.builder()
                .id(answerDto.id())
                .content(answerDto.content())
                .hit(answerDto.hit())
                .member(answerDto.memberDto().toMember())
                .board(answerDto.boardDto().toBoard())
                .build();
    }
}
