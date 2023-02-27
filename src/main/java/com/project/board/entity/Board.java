package com.project.board.entity;

import com.project.board.dto.BoardDto;
import com.project.board.type.Category;
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
@ToString(of = {"title", "content", "tagName", "hit", "countVisit", "category", "member"})
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    private String title;
    private String content;
    private String tagName;
    private Integer hit;
    private Integer countVisit;
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @CreatedDate
    LocalDateTime createdDate;
    @LastModifiedDate
    LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<BoardTag> boardTags = new ArrayList<>();


    @Builder
    public Board(Long id, String title, String content, String tagName, int hit, int countVisit, Category category, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.tagName = tagName;
        this.countVisit = countVisit;
        this.category = category;
        this.member = member;
    }
    @Builder
    public Board(String title, String content, String tagName, Category category) {
        this.title = title;
        this.content = content;
        this.tagName = tagName;
        this.category = category;
    }

    public static Board of(Long id, String title, String content, String tagName, int hit, int countVisit, Category category, Member member){
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .tagName(tagName)
                .hit(hit)
                .countVisit(countVisit)
                .category(category)
                .countVisit(countVisit)
                .member(member)
                .build();
    }



    public Board update(BoardDto board){
        return Board.builder()
                .title(board.title())
                .content(board.content())
                .category(board.category())
                .build();
    }
}
