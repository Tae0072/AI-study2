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

    public List<BoardResponse.ListDTO> 게시글목록보기(int page) {
        int limit = 3; // 한 페이지에 보여줄 게시물 수
        int offset = page * limit; // 시작인덱스
        List<Board> boardList = boardRepository.findAllWithLimitAndOffset(limit, offset); // 페이징된 게시글 목록
        return boardList.stream().map(BoardResponse.ListDTO::new).toList(); // DTO로 변환하여 반환
    }

}
