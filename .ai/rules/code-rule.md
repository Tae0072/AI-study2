# Code Rules

## 1. 명명 규칙 (Naming Conventions)

### 1.1 파일 및 뷰 템플릿
- **규칙**: 모든 뷰 템플릿(`.mustache`) 및 정적 리소스 파일명은 **kebab-case**를 사용한다.
- **예시**:
    - `join-form.mustache` (O) / `joinForm.mustache` (X)
    - `login-form.mustache` (O) / `loginForm.mustache` (X)
    - `save-form.mustache` (O) / `saveForm.mustache` (X)
    - `update-form.mustache` (O) / `updateForm.mustache` (X)

### 1.2 URL 경로
- **규칙**: RESTful API 및 페이지 이동 URL 경로는 **kebab-case**를 사용한다.
- **예시**: `/user/join-form`, `/board/save-form`

### 1.3 클래스 및 변수 (Java/Spring)
- **클래스**: PascalCase (예: `UserController`, `BoardService`)
- **변수 및 메서드**: camelCase (예: `joinUser()`, `userService`)
- **DTO**: 내부 클래스 또는 독립 클래스로 작성하며 목적을 명확히 함 (예: `UserRequest.JoinDTO`)
