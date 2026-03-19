# 🚩 작업 보고서: 로그인/로그아웃 기능 구현

- **작업 일시**: 2026-03-19
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)

1.  **DTO 설계**: 로그인 요청 데이터를 안전하게 전달하기 위해 `UserRequest.LoginDTO`를 생성하고 유효성 검사(@NotBlank)를 적용했습니다.
2.  **서비스 로직 구현**: `UserService.login` 메서드에서 입력받은 아이디로 사용자를 조회하고, BCrypt를 이용해 암호화된 비밀번호를 검증하는 로직을 작성했습니다. (초기 데이터인 `data.sql`과의 호환을 위해 평문 비교 로직도 포함)
3.  **컨트롤러 핸들러 작성**: `UserController`에 로그인 폼 이동, 로그인 처리, 로그아웃 처리 핸들러를 추가했습니다. 로그인 성공 시 사용자 정보를 세션(`HttpSession`)에 저장하고 메인 페이지로 리다이렉트합니다.
4.  **UI 구현**: Bootstrap을 활용하여 직관적인 `login-form.mustache`를 작성했습니다.
5.  **레이아웃 연동**: `header.mustache`에서 Mustache의 섹션 태그(`{{#sessionUser}}`, `{{^sessionUser}}`)를 사용하여 로그인 상태에 따라 메뉴가 동적으로 바뀌도록 수정했습니다.
6.  **설정 확인**: `application.properties`에서 세션 속성이 템플릿에 노출되도록 설정되어 있는지 최종 점검했습니다.

## 2. 🧩 변경된 모든 코드 포함

### 2.1 요청 데이터 모델 (UserRequest.java)
```java
public static class LoginDTO {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;
    
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    // Getter, Setter 생략
}
```

### 2.2 비즈니스 로직 (UserService.java)
```java
public User login(UserRequest.LoginDTO loginDTO) {
    // 1. 아이디로 사용자 조회
    User user = userRepository.findByUsername(loginDTO.getUsername())
            .orElseThrow(() -> new Exception400("아이디가 존재하지 않습니다."));

    // 2. 비밀번호 검증 (BCrypt 암호화 비교 + 평문 비교)
    if (BCrypt.checkpw(loginDTO.getPassword(), user.getPassword()) || loginDTO.getPassword().equals(user.getPassword())) {
        return user;
    } else {
        throw new Exception400("비밀번호가 틀렸습니다.");
    }
}
```

### 2.3 컨트롤러 (UserController.java)
```java
@PostMapping("/login")
public String login(@Valid UserRequest.LoginDTO loginDTO, BindingResult bindingResult) {
    // 서비스에서 인증 처리 후 세션 유저 객체 반환
    User sessionUser = userService.login(loginDTO);
    // 세션에 저장하여 로그인 상태 유지
    session.setAttribute("sessionUser", sessionUser);
    return "redirect:/";
}

@GetMapping("/logout")
public String logout() {
    // 세션 무효화하여 로그아웃 처리
    session.invalidate();
    return "redirect:/";
}
```

### 2.4 동적 메뉴 처리 (header.mustache)
```html
<ul class="navbar-nav">
    {{#sessionUser}} {{! 로그인했을 때만 보이는 메뉴 }}
        <li class="nav-item"><a class="nav-link" href="/board/save-form">글쓰기</a></li>
        <li class="nav-item"><a class="nav-link" href="/logout">로그아웃</a></li>
    {{/sessionUser}}
    {{^sessionUser}} {{! 로그인하지 않았을 때 보이는 메뉴 }}
        <li class="nav-item"><a class="nav-link" href="/join-form">회원가입</a></li>
        <li class="nav-item"><a class="nav-link" href="/login-form">로그인</a></li>
    {{/sessionUser}}
</ul>
```

## 3. 🍦 상세비유 쉬운 예시 (Easy Analogy)

"이번 작업은 **'놀이공원 자유이용권'**을 발급받는 과정과 같습니다.
- **로그인**: 매표소에서 내 이름(아이디)과 비밀번호를 대조하여 본인임을 확인받는 과정입니다.
- **세션(sessionUser)**: 본인 확인이 되면 손목에 채워주는 '자유이용권 팔찌'입니다. 이 팔찌를 차고 있으면 놀이기구(글쓰기, 정보수정)를 탈 때마다 매번 신분증을 보여줄 필요가 없습니다.
- **로그아웃**: 집에 갈 때 손목 팔찌를 가위로 잘라버리는(세션 무효화) 과정입니다. 팔찌가 없으면 더 이상 놀이기구를 이용할 수 없습니다."

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **BCrypt**: 비밀번호를 안전하게 저장하기 위한 단방향 해시 알고리즘입니다. 같은 비밀번호라도 매번 다른 해시값을 생성하는 '솔팅(Salting)' 기법을 사용하여 보안성이 매우 높습니다.
- **HttpSession**: 서버 측에 사용자 정보를 저장하는 기술입니다. 클라이언트는 `JSESSIONID`라는 쿠키만 가지고 있으며, 서버는 이 ID를 통해 어떤 사용자인지 식별합니다.
- **Mustache Section**: `{{#key}}`는 데이터가 존재할 때(true), `{{^key}}`는 데이터가 존재하지 않을 때(false) 실행되는 조건문 역할을 합니다. 이를 통해 로그인 여부에 따른 화면 분기를 간단히 처리할 수 있습니다.
