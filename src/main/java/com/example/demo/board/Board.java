package com.example.demo.board;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.demo.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "board_tb")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 게시글 번호
    private String title; // 제목
    private String content; // 내용

    // RULE: 모든 연관관계는 Lazy로 한다. OSIV는 false이다.
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 작성자

    @CreationTimestamp
    private LocalDateTime createdAt; // 작성일


    @Builder
    public Board(Integer id, String title, String content, User user, LocalDateTime createdAt) {
        this.id = id; // 게시글 번호
        this.title = title; // 제목
        this.content = content; // 내용
        this.user = user; // 작성자
        this.createdAt = createdAt; // 작성일
    }

}
