package com.example.demo.board;

import lombok.Data;

import java.sql.Timestamp;
import lombok.Data;
import java.time.format.DateTimeFormatter;

public class BoardResponse {

    @Data
    public static class ListDTO {
        private Integer id; // 게시글 번호
        private String title; // 제목
        private String username; // 작성자
        private String createdAt; // 작성일

        public ListDTO(Board board) {
            this.id = board.getId(); // 게시글 번호
            this.title = board.getTitle();  // 제목
            this.username = board.getUser().getUsername(); // 작성자
            this.createdAt = board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); // 작성일
        }
    }

    // RULE: Detail DTO는 상세 정보를 저장한다.
    @Data
    public static class Detail {

    }
}
