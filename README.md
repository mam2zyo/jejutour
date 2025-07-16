
##  Jejutrip - 개발 환경 설정 안내

### 🔧 데이터베이스 설정

기본적으로 **MySQL**을 사용하도록 설정되어 있습니다.
테스트나 로컬 환경에서 **H2 데이터베이스**를 사용하려면 아래 주석을 해제하세요.

**기본(MySQL) 설정 (application.yml)**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jejutrip?useSSL=false
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 1234

  jpa:
    database: mysql
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update
      show_sql: true
      format_sql: true
```

**H2 데이터베이스 사용 시 (주석 해제 필요)**

```yaml
# spring:
#   datasource:
#     url: jdbc:h2:mem:testdb
#     driver-class-name: org.h2.Driver
#     username: sa
#     password:
#
#   jpa:
#     database-platform: org.hibernate.dialect.H2Dialect
#     hibernate:
#       ddl-auto: update
#     show-sql: true
#
#   h2:
#     console:
#       enabled: true
#       path: /h2-console
```

>  테스트 용도로 H2를 사용할 경우, `application.yml`의 MySQL 설정을 주석 처리하고 위 H2 설정의 주석을 제거하세요.

---

### 테스트용 계정

애플리케이션 구동 시, 기본 관리자 및 일반 사용자 계정이 자동으로 생성됩니다.
API 테스트 시 아래 계정 정보를 사용하세요:

| 구분     | 이메일                     | 비밀번호            | 권한    |
| ------ | ----------------------- | --------------- | ----- |
| 관리자    | `codecraft@example.com` | `jejutrip#2025` | ADMIN |
| 일반 사용자 | `hello@google.com`      | `jejutrip#2025` | USER  |

> 비밀번호는 암호화되어 저장되며, `TestUserGenerator` 클래스에서 초기 데이터가 생성됩니다.

---
