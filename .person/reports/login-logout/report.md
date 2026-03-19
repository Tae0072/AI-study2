# 🚩 작업 보고서: 로그인/로그아웃 기능 및 BCrypt 보안 적용

- **작업 일시**: 2026-03-19
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)

1.  **인증 DTO 및 리포지토리 설정**: `LoginDTO`를 통해 로그인 요청 데이터를 받고, `UserRepository`에서 `username`으로 사용자를 찾는 기반을 마련했습니다.
2.  **보안 강화 (BCrypt)**: 비밀번호를 평문으로 저장하지 않고 `BCrypt` 해시 알고리즘을 적용했습니다. 초기 더미 데이터(`data.sql`)에도 실제 검증된 '1234'의 해시값을 반영했습니다.
3.  **로그인 로직 고도화**: `UserService.login`에서 저장된 해시값과 입력된 평문 비밀번호를 `BCrypt.checkpw()`로 비교하도록 구현했습니다.
4.  **세션 관리**: 로그인 성공 시 `HttpSession`을 통해 `sessionUser` 객체를 저장하여 사용자 인증 상태를 유지하게 했습니다.
5.  **UI/UX 동적 처리**: `header.mustache`에서 Mustache 섹션 태그를 사용하여 로그인 여부에 따라 '로그인/회원가입' 또는 '글쓰기/로그아웃' 메뉴가 보이도록 처리했습니다.

## 2. 🧩 변경된 모든 코드 포함

### 2.1 보안이 적용된 서비스 로직 (UserService.java)
```java
public User login(UserRequest.LoginDTO loginDTO) {
    // 1. 아이디 존재 여부 확인
    User user = userRepository.findByUsername(loginDTO.getUsername())
            .orElseThrow(() -> new Exception400("아이디가 존재하지 않습니다."));

    // 2. BCrypt를 이용한 비밀번호 일치 여부 검증(해시값 비교)
    if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
        throw new Exception400("비밀번호가 틀렸습니다.");
    }

    return user;
}
```

### 2.2 초기 데이터 보안 업데이트 (data.sql)
```sql
-- 평문 '1234' 대신 정확한 BCrypt 해시값 저장
INSERT INTO user_tb (username, password, created_at) 
VALUES ('ssar', '$2a$10$v2smN3fzz4YAwUyxTtcBN.iMIsgi0BZUUMgnqnSvndLp2LheBprVm', NOW());
INSERT INTO user_tb (username, password, created_at) 
VALUES ('cos', '$2a$10$v2smN3fzz4YAwUyxTtcBN.iMIsgi0BZUUMgnqnSvndLp2LheBprVm', NOW());
```

### 2.3 세션 기반 컨트롤러 (UserController.java)
```java
@PostMapping("/login")
public String login(@Valid UserRequest.LoginDTO loginDTO, BindingResult bindingResult) {
    User sessionUser = userService.login(loginDTO); // 인증 수행
    session.setAttribute("sessionUser", sessionUser); // 세션에 저장 (팔찌 채워주기)
    return "redirect:/";
}

@GetMapping("/logout")
public String logout() {
    session.invalidate(); // 세션 무효화 (팔찌 제거)
    return "redirect:/";
}
```

### 2.4 조건부 레이아웃 (header.mustache)
```html
<ul class="navbar-nav">
    {{#sessionUser}} {{! 로그인 완료 시 }}
        <li class="nav-item"><a class="nav-link" href="/board/save-form">글쓰기</a></li>
        <li class="nav-item"><a class="nav-link" href="/logout">로그아웃</a></li>
    {{/sessionUser}}
    {{^sessionUser}} {{! 로그인 전 }}
        <li class="nav-item"><a class="nav-link" href="/join-form">회원가입</a></li>
        <li class="nav-item"><a class="nav-link" href="/login-form">로그인</a></li>
    {{/sessionUser}}
</ul>
```

## 3. 🍦 상세비유 쉬운 예시 (Easy Analogy)

"이번 작업은 **'금고 비밀번호 체계 교체'**와 같습니다.
- **기존 방식**: 금고 포스트잇에 비밀번호 '1234'를 그대로 써붙여 놓은 것과 같아 위험했습니다.
- **변경된 방식**: 이제 금고에는 '1234'라는 글자 대신, 복잡한 암호문(`$2a$10$v2sm...`)이 적혀 있습니다. 도둑이 금고 문을 열어봐도 원래 비밀번호가 무엇인지 알 수 없습니다.
- **인증**: 사용자가 '1234'를 입력하면, 시스템이 마법의 기계(BCrypt)에 넣어 암호문을 만든 뒤, 금고에 적힌 암호문과 비교합니다. 결과가 맞으면 금고가 열립니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **BCrypt Salting**: 동일한 '1234'라도 생성할 때마다 해시값이 달라질 수 있는 이유는 내부적으로 무작위 문자열인 '솔트(Salt)'를 섞기 때문입니다. 이는 해시값을 미리 계산해두는 레인보우 테이블 공격을 방어합니다.
- **Session Timeout**: `HttpSession`에 저장된 정보는 서버 메모리에 유지되며, 일정 시간 활동이 없거나 브라우저를 끄면(설정에 따라) 자동으로 사라져 보안을 유지합니다.
- **Mustache Section**: `{{#sessionUser}}` 태그는 단순히 '값이 있느냐'를 판단하는 것을 넘어, 서버로부터 전달된 객체가 존재할 때만 해당 HTML 영역을 렌더링하도록 돕는 강력한 템플릿 엔진 기능입니다.
