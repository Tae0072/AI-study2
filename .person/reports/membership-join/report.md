# 회원가입 기능 완료 보고서 (Phase 2)

## 1. 개요
회원 인증 시스템의 핵심인 회원가입 기능을 완료하였습니다. 보안(BCrypt)과 유효성 검사(AOP)를 강화하여 안정적인 시스템 기반을 마련했습니다.

## 2. 작업 내용

### **[Step 2: AOP 유효성 검사]**
- **내용**: 컨트롤러의 유효성 검사 로직을 공통화했습니다.
- **적용 기술**: Spring AOP (`@Aspect`, `@Before`), `BindingResult`.
- **결과**: `MyValidationAdvice`를 통해 모든 컨트롤러에서 `@Valid` 에러를 자동으로 감지하고 `Exception400`을 던지도록 구현했습니다.

### **[Step 3: BCrypt 암호화]**
- **내용**: 사용자 비밀번호를 안전하게 보호하기 위해 단방향 해시 암호화를 적용했습니다.
- **적용 기술**: `jbcrypt` 라이브러리.
- **결과**: `UserService.join` 호출 시 비밀번호를 솔팅(Salting)하여 해싱 처리 후 DB에 저장합니다.

### **[Finalize: 회원가입 최종 구현]**
- **내용**: 프론트엔드와 백엔드를 연결하여 전체 회원가입 프로세스를 완성했습니다.
- **주요 파일**: 
    - `UserController.java`: `POST /join` 매핑.
    - `UserService.java`: 중복 체크 및 암호화 저장 로직.
    - `join-form.mustache`: 폼 데이터 전송 및 중복 체크 스크립트.

## 3. 예외 처리 기반 구축 (Phase 5 선행 작업)
- `GlobalExceptionHandler`를 구현하여 커스텀 예외(`Exception400`~`500`) 발생 시 일관된 JSON 응답(`Resp.fail`)을 제공합니다.

## 4. 이슈 및 해결 방법 (Issue & Resolution)
- **이슈**: `build.gradle`에 설정된 Spring Boot 4.0.3 버전을 찾지 못해 의존성 로드 실패 및 컴파일 오류 발생.
- **해결**: 버전을 실제 안정 버전인 `3.4.3`으로 변경하여 해결.

## 5. 향후 계획
- Phase 2.2: 로그인/로그아웃 기능 구현 (세션 기반 인증).
