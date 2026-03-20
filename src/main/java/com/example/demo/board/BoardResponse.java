package com.example.demo.board;

import lombok.Data;

import java.sql.Timestamp;
import lombok.Data;
import java.time.format.DateTimeFormatter;

public class BoardResponse {

    @Data
    public static class ListDTO {
        private Integer id;
        private String title;
        private String username;
        private String createdAt;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.username = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    }

    // RULE: Detail DTO는 상세 정보를 저장한다.
    @Data
    public static class Detail {

    }
}
