package com.project.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"content"})
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @Builder
    public Comment(Long id, String content, Member member, Answer answer) {
        this.id = id;
        this.content = content;
        this.member = member;
        if(answer != null){
            changeAnswer(answer);
        }
    }

    private void changeAnswer(Answer answer) {
        if(this.answer != null){
            this.answer.getComments().remove(this);
        }
        this.answer = answer;
        answer.getComments().add(this);
    }
}
