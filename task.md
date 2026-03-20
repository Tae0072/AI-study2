# 상세 계획 및 작업 현황(Detailed Tasks)

## Phase 1: 인프라 및 핵심 도메인 구축 (Infrastructure & Core Domain)
- [x] **T-1.1 프로젝트 초기 설정**
  - [x] Spring Boot 3.x, Java 21 설정 및 검토
  - [x] Dependencies 설정 (Spring Data JPA, H2, Mustache, Validation, Lombok)
- [x] **T-1.2 사용자/게시글/댓글 엔티티 모델링**
  - [x] `User` 엔티티 (id, username, password, email, address 등)
  - [x] `Board` 엔티티 (id, title, content, user, createdAt)
  - [x] `Reply` 엔티티 (id, comment, user, board, createdAt)
- [x] **T-1.3 공통 응답 객체 설계**
  - [x] `Resp<T>` 클래스 설계 (성공/실패 응답 메시지 및 데이터 구조 통일)
- [x] **T-1.4 H2 데이터베이스 및 JPA 환경 설정**
  - [x] `application.properties` 설정 (H2 Console, Hibernate Logging)
  - [x] 초기 데이터 구성을 위한 `data.sql` 작성
- [x] **T-1.5 레이아웃 템플릿 제작**
  - [x] `frontend-design` 스킬을 통한 `header.mustache`, `footer.mustache` 구성 및 공통 적용
  - [x] Bootstrap 기반 디자인 가이드라인 및 공통 스타일 적용

## Phase 2: 회원 관리 시스템 (Membership System)

- [x] **T-2.1 회원가입 기능 구현**
  - [x] `frontend-design` 스킬로 회원가입 폼(`join-form.mustache`) 구현
  - [x] `UserRequest.JoinDTO` 구현 및 유효성 검사 적용
  - [x] 아이디 중복 체크 API 구현 및 비동기 처리
  - [x] 패스워드 암호화 로직 적용 (BCrypt 사용 검토)
- [x] **T-2.2 로그인/로그아웃 기능 구현**
  - [x] `frontend-design` 스킬로 로그인 폼(`login-form.mustache`) 구현
  - [x] `UserRequest.LoginDTO` 구현
  - [x] 세션(`HttpSession`)을 이용한 사용자 로그인 상태 유지
  - [x] 로그인 성공/실패 시 예외 처리 및 알림 기능 구현
- [x] **T-2.3 회원 정보 수정 기능 구현**
  - [x] `frontend-design` 스킬로 정보 수정 폼 (`update-form.mustache`) 구현
  - [x] 회원 정보 수정 API 구현
  - [x] 회원 탈퇴 기능 및 데이터 처리 로직 구현 (Soft delete 또는 Cascade delete 전략 검토)
- [ ] **T-2.4 인증된 사용자 전용 접근 제어를 위한 Interceptor 구현**
  - [ ] 세션 체크 로직 공통화

## Phase 3: 게시판 핵심 기능 (Core Blog Features)

- [ ] **T-3.1 게시판 CRUD 기능 구현**
  - [ ] `frontend-design` 스킬로 게시글 목록 (`list.mustache`), 상세 보기 (`detail.mustache`), 글쓰기 (`save-form.mustache`), 수정 (`update-form.mustache`) 구현
  - [ ] 게시글 목록 조회 (`BoardRepository.findAll`)
  - [ ] 게시글 상세 보기 (댓글 목록 포함)
  - [ ] 게시글 작성/수정/삭제 API 및 권한 검증

- [x] **T-3.2 [페이징] 데이터베이스 연동: DB 쿼리 (LIMIT/OFFSET)**
  - [x] `BoardRepository`에 Native Query를 사용하여 `LIMIT 3 OFFSET ?` 쿼리 작성
  - [x] `BoardService`에서 페이지 번호를 매개변수로 받아 쿼리를 호출하는 기능 구현
- [x] **T-3.3 [페이징] 컨트롤러 연동: 파라미터 수집**
  - [x] `BoardController`에서 `@RequestParam`을 이용한 페이지 번호 수집 (기본값 0)
  - [x] 조회된 게시글 목록을 Mustache 템플릿으로 전달
- [x] **T-3.4 [페이징] 전체 페이지 수 계산**
  - [x] `BoardRepository`에서 전체 게시글 수(`COUNT(*)`) 조회 쿼리 작성
  - [x] `BoardService`에서 전체 페이지 수 계산 및 페이징 정보(현재 페이지, 이전/다음 존재 여부) 생성 
- [x] **T-3.5 [페이징] [이전]/[다음] 버튼 구현**
  - [x] Mustache 템플릿에서 `isFirst`, `isLast` 등을 사용하여 [이전], [다음] 버튼의 활성화 여부 제어   
  - [x] 쿼리 파라미터(`?page=n`)를 이용한 페이지 이동 기능 구현
- [x] **T-3.6 [페이징] 페이지 번호 목록(Pagination) 구현**
  - [x] 현재 페이지를 기준으로 이동 가능한 페이지 번호 목록 계산
  - [x] Mustache의 `{{#...}}` 문법을 사용하여 페이지 번호 링크를 반복 출력
- [ ] **T-3.7 게시글 검색 기능 구현**
  - [ ] 제목 또는 내용 기반 검색 기능 (`Containing` 키워드 사용)
  - [ ] 검색 결과에 대한 페이징 UI 연동
- [ ] **T-3.8 게시글 상세 보기 및 조회수 증가 로직**

## Phase 4: 상호작용 및 부가 기능 (Interaction Features)

- [ ] **T-4.1 댓글 작성 기능 구현**
  - [ ] 게시글 상세 페이지 하단 댓글 입력 UI (`detail.mustache` 내) 구현
  - [ ] `ReplyRequest.SaveDTO` 구현
  - [ ] AJAX 또는 Form 방식을 이용한 댓글 저장 로직

- [ ] **T-4.2 댓글 삭제 기능 구현**
  - [ ] 댓글 삭제 API 구현 및 권한 체크 로직 적용
  - [ ] 사용자 경험을 위한 UI 업데이트 로직

- [ ] **T-4.3 게시글 상세 페이지의 댓글 목록**
  - [ ] 게시글 조회 시 댓글 목록을 Fetch Join 등으로 성능 최적화하여 조회
  - [ ] 댓글 개수 표시 기능 추가

## Phase 5: 성능 최적화 및 유효성 검사 (Polishing & Validation)

- [x] **T-5.1 예외 처리 공통화 (GlobalExceptionHandler)**
  - [x] `@ControllerAdvice`를 활용한 전역 예외 처리
  - [x] REST API용 응답 포맷 구성
- [x] **T-5.2 데이터 유효성 검사(Bean Validation)**
  - [x] 주요 Request DTO에 `@NotBlank`, `@Size` 등 제약 조건 적용
  - [x] BindingResult를 이용한 에러 메시지 처리 (AOP 적용)
- [ ] **T-5.3 전체 기능 통합 테스트**
  - [ ] 시나리오 기반 테스트(로그인 -> 글쓰기 -> 댓글) 수행
  - [ ] 성능 이슈 분석(N+1 문제 등) 및 인덱스 최적화 검토

---

## [Step 1] 게시글 목록 보기 기능 완료
- [x] T-1.1 `data.sql`에 20개 이상의 게시글 데이터 추가 (페이징 테스트용)
- [x] T-1.2 전체 게시물 목록 보기 기능 구현 (`BoardService.게시글목록보기`)
- [x] T-1.3 `list.mustache` 페이지에 목록 데이터 출력

## [Step 2] SQL 페이징 쿼리 및 서비스 로직 적용
- [x] T-2.1 `OFFSET`과 `LIMIT`을 사용하여 `page` 파라미터에 맞는 게시글 조회 쿼리 작성

## [Step 3] UI 개선: 이전/다음 버튼
- [x] T-3.1 Mustache 템플릿에 `이전`, `다음` 버튼 추가 및 '이동 링크' 기능 구현

## [Step 4] 전체 페이지 수 및 번호 목록 UI
- [x] T-4.1 `SELECT COUNT(*)` 쿼리 작성 및 전체 게시글 수 조회
- [x] T-4.2 페이지 버튼 개수를 이용한 '페이지번호목록' 구현
- [x] T-4.3 Mustache에서 `1 2 3 4...` 등의 페이지 번호 목록 출력

## [Step 5] 유효성 및 버튼 상태 제어
- [x] T-5.1 첫 페이지와 마지막 페이지에서 버튼 비활성화(`disabled`) 처리
- [x] T-5.2 현재 페이지 번호 강조 처리 등 UI 보완
