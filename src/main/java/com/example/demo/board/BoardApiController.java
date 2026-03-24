package com.example.demo.board;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo._core.utils.Resp;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private final BoardService boardService;

    // RULE: ApiController는 ajax가 필요 할때만 사용한다.
    // Overdrive: 실시간 반응형 검색을 위해 비동기 요청을 처리한다.
    @GetMapping("/api/board/search")
    public ResponseEntity<?> search(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        
        // 검색어가 비어있을 경우 전체 목록 보기로 대체 가능하지만, 
        // 서비스의 '게시글검색하기'에서 빈 문자열 처리를 수행하도록 설계됨.
        BoardResponse.ListPageDTO listPageDTO = boardService.게시글검색하기(page - 1, keyword);
        
        return Resp.ok(listPageDTO);
    }
}
