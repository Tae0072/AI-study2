# 게시글 페이징 Step 1 작업 보고서

## 작업 일시
2026-03-20

## 수행 작업
- [x] T-1.1 `data.sql`에 20개 이상의 게시글 데이터 추가
- [x] T-1.2 페이징 없는 전체 게시글 목록 보기 기능 구현 (`BoardService.게시글목록보기`)
- [x] T-1.3 `list.mustache` 생성 및 전체 목록 출력 확인

## 상세 내용
### 1. 데이터 베이스
- `data.sql`에 20개의 더미 데이터를 추가하여 총 23개의 게시글 확보.

### 2. 백엔드 로직
- `BoardResponse.ListDTO`: id, title, username, createdAt 필드를 가지며 Entity를 DTO로 변환하는 생성자 포함.
- `BoardService.게시글목록보기()`: `boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))`를 사용하여 최신순 정렬 구현.
- `BoardController.list()`: `/board/list` 요청 처리 및 `Model`에 `boardList` 바인딩.

### 3. 프론트엔드
- `board/list.mustache`: 테이블 구조로 게시글 목록 출력. 상단에 번호, 제목, 작성자, 작성일 표시.

## 결과 확인
- `/board/list` 접속 시 23개의 게시글이 최신순(ID 23번부터)으로 출력됨을 확인.
