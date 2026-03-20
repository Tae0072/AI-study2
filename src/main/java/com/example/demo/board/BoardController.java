package com.example.demo.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService; // 게시글 서비스
    private final HttpSession session; // 세션

    // 게시글 목록 페이지
    @GetMapping({"/", "/home", "/board/list"})
    public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        // 페이징된 게시글 목록
        List<BoardResponse.ListDTO> boardList = boardService.게시글목록보기(page);
        model.addAttribute("models", boardList); // 게시글 목록
        return "board/list"; // 게시글 목록 페이지로 이동
    }
}
