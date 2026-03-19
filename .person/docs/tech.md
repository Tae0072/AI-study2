# 기술 명세서 (tech.md)

## 1. 백엔드 (Backend)
- **Framework**: Spring Boot 4.0.3
- **Language**: Java 21 (v21)
- **Build Tool**: Gradle
- **Authentication**: 서버 세션 방식 (HttpSession 활용)
- **Architecture Strategy**:
    - **Controller 분리**: 화면 이동 전용 `@Controller`와 비동기 통신 전용 `@RestController`를 명확히 분리하여 관리한다.
    - **DTO 기반 개발**: 모든 API 입출력에 전용 DTO(`*Request`, `*Response`)를 사용하며, Entity를 직접 외부로 노출하지 않는다.
    - **Service Layer**: 비즈니스 로직 처리 및 Entity-DTO 간의 변환을 담당한다.

## 2. 데이터베이스 및 영속성 (Database & Persistence)
- **Database**: H2 Database
- **ORM**: Spring Data JPA
- **Persistence Strategy**:
    - **Lazy Loading**: 모든 연관관계는 `FetchType.LAZY`를 원칙으로 하며, 필요한 경우에만 Fetch Join이나 DTO 직접 조회를 사용한다.
    - **Hard Delete**: 회원 탈퇴 및 게시글 삭제 시 관련 데이터를 DB에서 물리적으로 삭제(Cascade)한다.
    - **OSIV (Open Session In View)**: `false`로 설정하여 영속성 컨텍스트의 범위를 트랜잭션 내로 제한한다.

## 3. 프론트엔드 (Frontend)
- **Template Engine**: Mustache
- **CSS Framework**: Bootstrap 5 (CDN 또는 로컬 라이브러리 활용)
- **Communication**: Fetch API / AJAX를 이용한 비동기 통신을 적극 활용하여 동적인 사용자 경험을 제공한다.
- **UI Structure**: 공통 레이아웃(Header, Footer)을 분리하여 재사용성을 높인다.

## 4. 공통 규약 (Common Convention)
- **API Response**: `_core/utils/Resp.java`를 활용하여 성공/실패 시 공통된 JSON 응답 형식을 유지한다.
- **Error Handling**: `@ControllerAdvice`를 통해 전역적으로 예외를 처리하고 적절한 응답 코드를 반환한다.
- **Logging**: 중요한 비즈니스 흐름 및 에러 발생 시 SLF4J를 통한 로그 기록을 수행한다.
