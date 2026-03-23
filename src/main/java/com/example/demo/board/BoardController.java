package com.example.demo.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import com.example.demo._core.handler.ex.Exception400;
import org.springframework.ui.Model;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService; // 게시글 서비스
    private final HttpSession session; // 세션

    // 게시글 목록 페이지
    @GetMapping({"/", "/home", "/board/list"})
    public String list(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        // 1. 유효성 검사 (1페이지 미만 요청 시 예외 처리)
        if (page < 1) {
            throw new Exception400("잘못된 페이지 번호입니다.");
        }

        // 2. 게시글 목록 보기 (서비스에는 0-based 인덱스 전달)
        List<BoardResponse.ListDTO> boardList = boardService.게시글목록보기(page - 1);
        
        // 3. 페이징 상태 계산 (Step 3 기초 로직)
        boolean isFirst = (page == 1); // 첫 페이지 여부
        boolean isLast = (boardList.size() < 3); // 마지막 페이지 여부 (3개 미만이면 마지막)
        int prevPage = page - 1; // 이전 페이지 번호
        int nextPage = page + 1; // 다음 페이지 번호

        // 4. 모델에 데이터 담기 (프로젝트 규칙 준수)
        model.addAttribute("models", boardList); // Rule b: 컬렉션
        model.addAttribute("model", new BoardResponse.PaginationDTO(isFirst, isLast, prevPage, nextPage, page)); // Rule a: 오브젝트

        return "board/list"; // 게시글 목록 페이지로 이동
    }
}
