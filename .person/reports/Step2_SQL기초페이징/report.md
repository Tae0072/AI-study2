# 🚩 작업 보고서: Step 2. SQL 기초 페이징 구현

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. 사용자의 요청(TODO의 Step 2 진행)을 확인하고 기존 리포지토리와 서비스 구조를 파악했습니다.
2. `BoardRepository`에 `LIMIT`과 `OFFSET`을 직관적으로 사용하는 네이티브 쿼리를 추가했습니다.
3. `BoardService`에서 페이지 번호를 넘겨받아 `OFFSET` 값을 수학적으로 계산하여 조회 로직을 적용했습니다.
4. `BoardController`에서 `@RequestParam`을 통해 URL에서 페이지 번호를 동적으로 받도록 연결했습니다.
5. TODO 리스트의 T-2.1과 T-2.2 항목을 모두 구현하고 완료 처리했습니다.

## 2. 🧩 변경된 매인 코드

```java
// 1. BoardRepository.java: 네이티브 쿼리로 LIMIT과 OFFSET 적용
public interface BoardRepository extends JpaRepository<Board, Integer> {
    // ... 기존 메서드 생략 ...
    @Query(value = "SELECT * FROM board_tb ORDER BY id DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Board> findAllWithLimitAndOffset(@Param("limit") int limit, @Param("offset") int offset);
}

// 2. BoardService.java: 페이지 번호에 따른 Offset 계산 로직
@Service
public class BoardService {
    // ...
    public List<BoardResponse.ListDTO> 게시글목록보기(int page) {
        int limit = 3; // 한 페이지당 보여줄 게시물 개수는 3개로 고정
        int offset = page * limit; // 넘겨받은 페이지 번호에 따라 건너뛸 데이터를 계산 (0번째, 3번째, 6번째...)
        
        List<Board> boardList = boardRepository.findAllWithLimitAndOffset(limit, offset);
        return boardList.stream().map(BoardResponse.ListDTO::new).toList();
    }
}

// 3. BoardController.java: 사용자 요청으로부터 page 값을 컨트롤러에서 수신
@Controller
public class BoardController {
    // ...
    @GetMapping({"/", "/home", "/board/list"})
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        // 클라이언트로부터 ?page=1 형태로 값을 받습니다. 값이 없으면 기본값인 0이 됩니다.
        List<BoardResponse.ListDTO> boardList = boardService.게시글목록보기(page);
        model.addAttribute("boardList", boardList);
        return "board/list";
    }
}
```

## 3. 🍦 상세비유 쉬운 예시를 들어서 (Easy Analogy)

이번 작업은 **백과사전을 3장씩만 잘라서 이북(e-book)으로 읽는 것**과 같습니다! 
마치 수천 페이지의 게시판이라는 거대한 책을 브라우저에 한꺼번에 던져주면 무겁고 렉이 걸리니까, 백엔드 데이터베이스에게 "한 번에 딱 3개(`LIMIT 3`)만 보여줘!"라고 약속하는 것과 비슷해요. 
그리고 `page=2`라고 알려주면 "앞에서 6장을 건너뛰고(`OFFSET 6`) 7번째 페이지부터 3장을 읽어줘"라고 지시하는 효율적인 배달 시스템을 구축한 것입니다.

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **[LIMIT & OFFSET]**: SQL 언어에서 결과물의 개수를 제한하고 특정 지점부터 시작하게 하는 마법의 키워드입니다.
  - `LIMIT 3`: 데이터베이스가 수백만 개의 데이터가 있어도 정확히 3개의 데이터만 찾고 바로 종료합니다. 서버 메모리를 절약하는 일등 공신입니다.
  - `OFFSET`: 결과를 가져오기 전에 폐기할(건너뛸) 데이터의 수입니다. 주의할 점은 OFFSET 값이 너무 커지면 데이터베이스가 그 앞의 데이터를 모두 스캔해야 하므로 성능이 저하될 수 있습니다. (이럴 땐 커서 기반 페이징을 고려해야 합니다!)
- **[@RequestParam(defaultValue = "0")]**: 주소창에 `?page=0`과 같이 적는 쿼리스트링을 자바 변수로 쏙 빼주는 스프링의 기능입니다. 사용자가 처음에 `localhost:8080/` 처럼 아무 값도 입력하지 않았을 때 에러가 나지 않도록 방어하기 위해 `defaultValue = "0"`을 설정하여 기본 0페이지(첫 페이지)가 출력되도록 안전망을 깔았습니다!
