# 🚩 작업 보고서: [Step 4] 전체 페이지 계산 및 슬라이딩 윈도우 UI 구현

- **작업 일시**: 2026-03-23
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **DTO 구조 고도화**: `BoardResponse.PaginationDTO` 내부에 `PageItem` 클래스를 정의하여 페이지 번호와 현재 페이지 여부(`isCurrent`)를 함께 담도록 설계했습니다. 또한 앞뒤 `...` 표시를 위한 `showPrevDots`, `showNextDots` 필드를 추가했습니다.
2. **슬라이딩 윈도우 로직 구현**: `BoardService`에서 현재 페이지를 중심으로 3개의 번호만 노출되도록 `startPage`와 `endPage`를 계산하는 알고리즘을 구현했습니다.
    - 윈도우 크기를 3으로 설정하여 `displayPage`가 중앙에 오도록 처리했습니다.
    - 첫 페이지나 마지막 페이지에 도달했을 때 윈도우가 범위를 벗어나지 않도록 보정 로직을 적용했습니다.
3. **... 생략 조건 설정**: `startPage > 1`일 경우 앞에 `...`을, `endPage < totalPage`일 경우 뒤에 `...`을 표시하도록 플래그를 설정했습니다.
4. **UI 최종 개선**: `list.mustache`에서 Mustache의 반복문과 조건문을 활용해 현재 페이지를 강조(`active`)하고 `...`을 적절한 위치에 출력하도록 수정했습니다.

## 2. 🧩 변경된 모든 코드 포함

### BoardResponse.java (DTO 확장)
```java
@Data
public static class PaginationDTO {
    // ... 기존 필드 생략 ...
    private boolean showPrevDots; // 앞쪽 ... 표시 여부
    private boolean showNextDots; // 뒤쪽 ... 표시 여부
    private java.util.List<PageItem> pageNumbers; // 표시할 페이지 객체 목록

    @Data
    public static class PageItem {
        private int number;       // 페이지 번호
        private boolean isCurrent; // 현재 페이지 여부
        public PageItem(int number, boolean isCurrent) {
            this.number = number;
            this.isCurrent = isCurrent;
        }
    }
}
```

### BoardService.java (슬라이딩 로직)
```java
// 7. 슬라이딩 윈도우 계산 (윈도우 크기: 3)
int windowSize = 3;
int startPage = Math.max(1, displayPage - (windowSize / 2));
int endPage = Math.min(totalPage, startPage + (windowSize - 1));

// startPage 다시 보정 (endPage가 limit에 걸릴 때)
startPage = Math.max(1, endPage - (windowSize - 1));

// 8. ... 표시 여부 결정
boolean showPrevDots = (startPage > 1);
boolean showNextDots = (endPage < totalPage);

// 9. 페이지 객체 생성
for (int i = startPage; i <= endPage; i++) {
    pageNumbers.add(new BoardResponse.PaginationDTO.PageItem(i, i == displayPage));
}
```

### list.mustache (UI 적용)
```html
<!-- 앞쪽 ... -->
{{#model.pagination.showPrevDots}}
<li class="page-item disabled"><span class="page-link">...</span></li>
{{/model.pagination.showPrevDots}}

<!-- 번호 리스트 (현재 페이지 강조) -->
{{#model.pagination.pageNumbers}}
<li class="page-item {{#isCurrent}}active{{/isCurrent}}">
    <a class="page-link" href="?page={{number}}">{{number}}</a>
</li>
{{/model.pagination.pageNumbers}}

<!-- 뒤쪽 ... -->
{{#model.pagination.showNextDots}}
<li class="page-item disabled"><span class="page-link">...</span></li>
{{/model.pagination.showNextDots}}
```

## 3. 🍦 상세비유 쉬운 예시를 들어서 (Easy Analogy)

"이번 작업은 **'긴 기차의 창문'**을 만드는 것과 같습니다. 기차가 아무리 길어도 승객은 창문을 통해 현재 자기가 탄 칸과 그 앞뒤 칸만 볼 수 있죠. 기차의 아주 앞이나 아주 뒤에 칸이 더 있다면 창문 밖으로 `...` 이라는 표지판이 보이는 셈입니다. 이제 승객은 수많은 페이지 중에서 자신이 있는 곳 주변만 집중해서 볼 수 있게 되어 훨씬 눈이 편안해졌습니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **Sliding Window Algorithm**: 방대한 데이터 집합에서 특정 범위(Window)만 추출하여 보여주는 방식입니다. 페이징 처리 시 모든 번호를 나열하면 UI가 깨질 수 있는데, 이를 통해 일정한 개수의 버튼만 유지할 수 있습니다.
- **Mustache active 처리**: Mustache는 `if(a == b)` 같은 비교 연산이 불가능합니다. 이를 해결하기 위해 서버(Service)에서 미리 `isCurrent`라는 불리언 값을 계산하여 DTO에 담아 보냄으로써, 템플릿 엔진의 제약을 극복했습니다.
