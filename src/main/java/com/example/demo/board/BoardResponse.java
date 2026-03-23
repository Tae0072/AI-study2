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

    // 페이징 정보를 담는 DTO
    @Data
    public static class PaginationDTO {
        private boolean isFirst; // 첫 페이지 여부
        private boolean isLast;  // 마지막 페이지 여부
        private int prevPage;    // 이전 페이지 번호
        private int nextPage;    // 다음 페이지 번호
        private int currentPage; // 현재 페이지 번호 (1-based)
        private int totalPage;   // 총 페이지 수
        private boolean showPrevDots; // 앞쪽 ... 표시 여부
        private boolean showNextDots; // 뒤쪽 ... 표시 여부
        private java.util.List<PageItem> pageNumbers; // 표시할 페이지 번호 객체 목록

        @Data
        public static class PageItem {
            private int number;       // 페이지 번호
            private boolean isCurrent; // 현재 페이지 여부

            public PageItem(int number, boolean isCurrent) {
                this.number = number;
                this.isCurrent = isCurrent;
            }
        }

        public PaginationDTO(boolean isFirst, boolean isLast, int prevPage, int nextPage, int currentPage, int totalPage, boolean showPrevDots, boolean showNextDots, java.util.List<PageItem> pageNumbers) {
            this.isFirst = isFirst;
            this.isLast = isLast;
            this.prevPage = prevPage;
            this.nextPage = nextPage;
            this.currentPage = currentPage;
            this.totalPage = totalPage;
            this.showPrevDots = showPrevDots;
            this.showNextDots = showNextDots;
            this.pageNumbers = pageNumbers;
        }
    }

    // 게시글 목록 페이지에 필요한 모든 데이터를 담는 통합 DTO
    @Data
    public static class ListPageDTO {
        private java.util.List<ListDTO> boardList; // 게시글 목록
        private PaginationDTO pagination;          // 페이징 정보

        public ListPageDTO(java.util.List<ListDTO> boardList, PaginationDTO pagination) {
            this.boardList = boardList;
            this.pagination = pagination;
        }
    }
}
