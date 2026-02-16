# logging-pattern-mask

JSON 직렬화 없이 순수 문자열 패턴 기반으로 동작하는 Logback 로그 마스킹 모듈입니다. 로그 출력 직전에 
문자열 전체에 대해 정규식 패턴을 적용하여 민감 정보를 마스킹합니다.

✨ 특징

* Logback PatternLayout 확장
* JSON 직렬화/역직렬화 불필요
* 정규식 기반 마스킹

📦 패키지 구조
```bash
sanghoon.study.logging.mask 
  ├─ core
  │   ├─ SensitiveStringSanitizer
  │   └─ DefaultSensitiveStringSanitizer
  └─ logback
      ├─ RuleSpecParser
      └─ MaskingPatternLayout
```

🚀 사용 방법

1️⃣ logback.xml 설정
```xml
<configuration>
  <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
      <layout class="sanghoon.study.logging.mask.logback.MaskingPatternLayout">
        <pattern>
          %d{HH:mm:ss.SSS} %-5level [%thread] %logger - %msg%n
        </pattern>
        <enabled>true</enabled>

        <!-- 사전 필터링 (선택), 필터링 없으면 전체 적용 -->
        <triggers>010-,@</triggers>

        <!-- rule format: name|regex|replacement -->
        <rule>
          phone|(\\b01[016789]-?)\\d{3,4}(-?\\d{4}\\b)|$1****$2
        </rule>
        <rule>
          email|([A-Za-z0-9._%+-]{2})[A-Za-z0-9._%+-]*(@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})|$1****$2
        </rule>
      </layout>
    </encoder>
  </appender>

  <root level="INFO">
    <appender-ref ref="CONSOLE"/>
  </root>

</configuration>
```

🧠 Rule 포맷 설명

> name | regex | replacement

| 항목 | 설명 |
| --- | --- |
| name	| 규칙 식별자 |
| regex | 정규식 패턴 | 
| replacement |	replaceAll() 치환 문자열 |

🔎 예시

입력 로그 예시
> phone=010-1234-5678, email=abcdef@gmail.com

출력 로그 예시
> phone=010-****-5678, email=ab****@gmail.com

🧪 Test API

```bash
POST /api/payload
```

**호출예시**

Request
```bash
curl -X POST "http://localhost:8080/api/payload" \
-H "Content-Type: application/json" \
-d '{"message":"phone=010-1234-5678, email=abcdef@gmail.com"}'
```

Response
```bash
{
    "code": 200,
    "status": "OK",
    "message": "OK",
    "data": {
        "message": "phone=010-1234-5678, email=abcdef@gmail.com",
        "created": "09:12:34.567"
    }
}
```

기대 로그 출력
```bash
received : phone=010-****-5678, email=ab****@gmail.com
```

🎯 동작 원리

1. Logback이 로그 메시지를 문자열로 생성
2. MaskingPatternLayout#doLayout() 실행
3. SensitiveStringSanitizer#sanitize() 호출
4. 등록된 Rule을 순차 적용
5. 마스킹된 문자열 반환

⚡ 성능 전략

* JSON 직렬화 없음
* 문자열 단일 패스 처리
* trigger 기반 사전 contains 필터
* Pattern은 초기화 시 1회 컴파일

📌 왜 Layout 기반인가?

* 로그 출력 직전 단 1회 처리
* AOP 대비 중복 실행 가능성 낮음
* 로그 레벨 필터링 이후 실행
* 프레임워크 독립적

⚠ 주의사항

* XML에서 \ 는 \\ 로 escape 필요
* $1, $2 는 정규식 그룹 기반 치환
* trigger가 설정되어 있으면 해당 키워드가 없을 경우 sanitize가 실행되지 않음

