package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "Board_Id")
    private Long Id; //Board id

    private String title; //제목

    private String writer; //작성자
    private String password; //게시글 비밀번호

    private LocalDateTime dateTime; //작성 시간
    private String content; //내용

    private Integer views; //조회수

    private Integer count; //댓글 총합

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id")
    private Member member;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
