package com.example.demo.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;

    @GetMapping({"/", "/home", "/board/list"})
    public String list(Model model) {
        List<BoardResponse.ListDTO> boardList = boardService.게시글목록보기();
        model.addAttribute("boardList", boardList);
        return "board/list";
    }
}
