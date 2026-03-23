package com.example.demo.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import com.example.demo._core.handler.ex.Exception400;
import org.springframework.ui.Model;

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

        // 2. 서비스에서 화면에 필요한 모든 데이터를 통합 DTO로 받기 (서비스에는 0-based 인덱스 전달)
        BoardResponse.ListPageDTO listPageDTO = boardService.게시글목록보기(page - 1);

        // 3. 모델에 데이터 한 번만 담기
        model.addAttribute("model", listPageDTO);

        return "board/list"; // 게시글 목록 페이지로 이동
    }
}
