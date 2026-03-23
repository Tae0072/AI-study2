# 🚩 작업 보고서: Step 3 UI 제어 및 이전/다음 버튼

- **작업 일시**: 2026-03-23
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1.  **DTO 설계**: 페이징 상태(첫/마지막 페이지 여부, 이전/다음 번호)를 담을 `PaginationDTO`를 `BoardResponse` 내부에 정의했습니다.
2.  **컨트롤러 로직 구현**: 
    *   기존 0기반 페이지 파라미터를 사용자가 이해하기 쉬운 **1기반(1-based)**으로 변경했습니다.
    *   `page < 1` 요청 시 `Exception400`을 던지는 유효성 검사를 추가했습니다.
    *   서비스 호출 시에는 DB 인덱스에 맞춰 `page - 1`을 전달하도록 수정했습니다.
3.  **UI 구현**: `list.mustache` 하단에 부트스트랩 Pagination 컴포넌트를 추가하고, 컨트롤러에서 넘겨준 데이터를 바탕으로 버튼의 활성화/비활성화 상태를 제어했습니다.

## 2. 🧩 변경된 모든 코드

### `BoardResponse.java` (페이징 데이터 구조)
```java
// 페이징 정보를 담는 DTO를 추가했습니다.
@Data
public static class PaginationDTO {
    private boolean isFirst; // 첫 페이지 여부 (이전 버튼 제어용)
    private boolean isLast;  // 마지막 페이지 여부 (다음 버튼 제어용)
    private int prevPage;    // 이전 페이지 번호
    private int nextPage;    // 다음 페이지 번호
    private int currentPage; // 현재 페이지 번호

    public PaginationDTO(boolean isFirst, boolean isLast, int prevPage, int nextPage, int currentPage) {
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.prevPage = prevPage;
        this.nextPage = nextPage;
        this.currentPage = currentPage;
    }
}
```

### `BoardController.java` (1-based 페이징 및 유효성 검사)
```java
@GetMapping({"/", "/home", "/board/list"})
public String list(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
    // 1. 유효성 검사: 1보다 작은 페이지 번호는 잘못된 요청입니다.
    if (page < 1) {
        throw new Exception400("잘못된 페이지 번호입니다.");
    }

    // 2. 서비스 호출: 내부 DB 로직은 여전히 0부터 시작하므로 page-1을 보냅니다.
    List<BoardResponse.ListDTO> boardList = boardService.게시글목록보기(page - 1);
    
    // 3. 페이징 상태 계산 (Step 3 기초 로직)
    boolean isFirst = (page == 1); 
    boolean isLast = (boardList.size() < 3); // 3개 미만이면 다음 데이터가 없다고 간주합니다.
    int prevPage = page - 1;
    int nextPage = page + 1;

    // 4. 모델에 담기: 프로젝트 규칙(a, b)을 준수합니다.
    model.addAttribute("models", boardList); // 컬렉션
    model.addAttribute("model", new BoardResponse.PaginationDTO(isFirst, isLast, prevPage, nextPage, page)); // 오브젝트

    return "board/list";
}
```

### `list.mustache` (화면 구현)
```html
<!-- 부트스트랩을 활용한 페이징 UI -->
<ul class="pagination justify-content-center">
    <!-- 첫 페이지면 '이전' 버튼을 클릭할 수 없게(disabled) 만듭니다. -->
    <li class="page-item {{#model.isFirst}}disabled{{/model.isFirst}}">
        <a class="page-link" href="?page={{model.prevPage}}">이전</a>
    </li>

    <li class="page-item active">
        <span class="page-link">{{model.currentPage}}</span>
    </li>

    <!-- 마지막 페이지면 '다음' 버튼을 클릭할 수 없게 만듭니다. -->
    <li class="page-item {{#model.isLast}}disabled{{/model.isLast}}">
        <a class="page-link" href="?page={{model.nextPage}}">다음</a>
    </li>
</ul>
```

## 3. 🍦 상세비유 (Easy Analogy)
"이번 작업은 **책의 쪽수(Page)**를 매기는 것과 같습니다. 책은 보통 1쪽부터 시작하지만, 도서관 컴퓨터 시스템은 내부적으로 0번 위치부터 데이터를 찾을 수도 있죠. 우리는 독자(사용자)가 0쪽이 아닌 1쪽부터 볼 수 있게 안내판을 바꿔 달고, 책장을 넘길 때 '이전'이나 '다음' 페이지가 없으면 손가락이 더 이상 안 넘어가게 막아둔 것과 비슷합니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

*   **1-based Paging**: 사용자는 1페이지부터 인지하지만 개발 환경은 0부터 시작하는 불일치를 해결하는 방식입니다. 컨트롤러가 그 중간 다리 역할을 하여 사용자에게 친숙한 숫자를 제공합니다.
*   **Mustache Conditional Rendering**: `{{#model.isFirst}}disabled{{/model.isFirst}}` 구문은 Mustache의 섹션 기능을 이용한 조건부 렌더링입니다. `isFirst`가 true일 때만 `disabled` 클래스가 HTML에 추가됩니다.
