package com.example.demo.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import java.util.List;

/**
 * DTO는 Service에서 만든다. Entity를 Controller에 전달하지 않는다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.ListDTO> 게시글목록보기() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Board> boardList = boardRepository.findAll(sort);
        return boardList.stream().map(BoardResponse.ListDTO::new).toList();
    }

}
