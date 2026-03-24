package com.example.demo.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * DTO는 Service에서 만든다. Entity를 Controller에 전달하지 않는다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository; // 게시글 리포지토리

    public BoardResponse.ListPageDTO 게시글목록보기(int page) {
        int limit = 5; // 한 페이지에 보여줄 게시물 수
        int offset = page * limit; // 시작인덱스
        
        // 1. 전체 게시글 수 조회
        long totalCount = boardRepository.count();
        
        // 2. 총 페이지 수 계산 (올림 처리)
        int totalPage = (int) Math.ceil((double) totalCount / limit);
        
        // 3. 유효성 검사 (최대 페이지 초과 시 404 예외 발생)
        if (page + 1 > totalPage && totalPage > 0) {
            throw new com.example.demo._core.handler.ex.Exception404("존재하지 않는 페이지입니다.");
        }

        // 4. 게시글 목록 조회 (Native Query)
        List<Board> boardList = boardRepository.findAllWithLimitAndOffset(limit, offset);

        // 5. 게시글 목록을 DTO로 변환
        List<BoardResponse.ListDTO> listDTOs = boardList.stream().map(BoardResponse.ListDTO::new).toList();

        // 6. 페이징 정보 계산 (1-based 페이지 번호로 계산)
        int displayPage = page + 1; // 화면에 표시될 페이지 번호 (1-based)
        boolean isFirst = (displayPage == 1); // 첫 페이지 여부
        boolean isLast = (displayPage >= totalPage); // 마지막 페이지 여부
        int prevPage = displayPage - 1; // 이전 페이지 번호
        int nextPage = displayPage + 1; // 다음 페이지 번호
        
        // 7. 슬라이딩 윈도우 페이지 번호 계산 (윈도우 크기: 3)
        int windowSize = 3;
        int startPage = Math.max(1, displayPage - (windowSize / 2));
        int endPage = Math.min(totalPage, startPage + (windowSize - 1));
        
        // startPage 보정 (endPage가 limit에 걸릴 경우)
        startPage = Math.max(1, endPage - (windowSize - 1));

        // 8. ... 표시 여부 결정
        boolean showPrevDots = (startPage > 1);
        boolean showNextDots = (endPage < totalPage);

        // 9. 페이지 번호 객체 목록 생성
        List<BoardResponse.PaginationDTO.PageItem> pageNumbers = new java.util.ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(new BoardResponse.PaginationDTO.PageItem(i, i == displayPage));
        }

        // 10. 페이징 정보 DTO 생성
        BoardResponse.PaginationDTO pagination = new BoardResponse.PaginationDTO(
            isFirst, isLast, prevPage, nextPage, displayPage, totalPage, showPrevDots, showNextDots, pageNumbers
        );

        // 11. 통합 DTO 반환
        return new BoardResponse.ListPageDTO(listDTOs, pagination);
    }

    public BoardResponse.ListPageDTO 게시글검색하기(int page, String keyword) {
        int limit = 3; // 한 페이지에 보여줄 게시물 수
        int offset = page * limit; // 시작인덱스
        
        // 1. 검색된 총 게시글 수 조회
        long totalCount = boardRepository.countWithSearch(keyword);
        
        // 2. 총 페이지 수 계산 (올림 처리)
        int totalPage = (int) Math.ceil((double) totalCount / limit);
        
        // 3. 유효성 검사 (검색 결과가 있을 때만 최대 페이지 초과 체크)
        if (page + 1 > totalPage && totalPage > 0) {
            throw new com.example.demo._core.handler.ex.Exception404("존재하지 않는 페이지입니다.");
        }

        // 4. 검색 및 페이징된 게시글 목록 조회
        List<Board> boardList = boardRepository.findAllWithSearchAndLimitAndOffset(keyword, limit, offset);

        // 5. 게시글 목록을 DTO로 변환
        List<BoardResponse.ListDTO> listDTOs = boardList.stream().map(BoardResponse.ListDTO::new).toList();

        // 6. 페이징 정보 계산
        int displayPage = page + 1;
        boolean isFirst = (displayPage == 1);
        boolean isLast = (displayPage >= totalPage || totalPage == 0);
        int prevPage = displayPage - 1;
        int nextPage = displayPage + 1;
        
        // 7. 슬라이딩 윈도우 페이지 번호 계산
        int windowSize = 3;
        int startPage = Math.max(1, displayPage - (windowSize / 2));
        int endPage = Math.min(totalPage, startPage + (windowSize - 1));
        if (totalPage > 0) {
            startPage = Math.max(1, endPage - (windowSize - 1));
        }

        // 8. ... 표시 여부 결정
        boolean showPrevDots = (startPage > 1);
        boolean showNextDots = (endPage < totalPage);

        // 9. 페이지 번호 객체 목록 생성
        List<BoardResponse.PaginationDTO.PageItem> pageNumbers = new java.util.ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(new BoardResponse.PaginationDTO.PageItem(i, i == displayPage));
        }

        // 10. 페이징 정보 DTO 생성
        BoardResponse.PaginationDTO pagination = new BoardResponse.PaginationDTO(
            isFirst, isLast, prevPage, nextPage, displayPage, totalPage, showPrevDots, showNextDots, pageNumbers
        );

        // 11. 통합 DTO 반환
        return new BoardResponse.ListPageDTO(listDTOs, pagination);
    }

}
