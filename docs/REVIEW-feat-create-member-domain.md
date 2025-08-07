# Code Review: feat/create-member-domain

## 개요
Member 도메인 클래스와 MemberStatus enum, 그리고 테스트 코드를 새로 추가한 브랜치에 대한 Kotlin 전문가 리뷰

## 📍 현재 구현 분석

### Member.kt
- **장점**: Primary constructor와 property 선언이 간결함
- **장점**: `check()` 함수를 사용하여 Kotlin 표준 라이브러리 활용

### MemberStatus.kt  
- **장점**: enum class 사용으로 타입 안전성 확보

### MemberTest.kt
- **장점**: AssertJ와 JUnit 5 활용
- **장점**: DisplayName 어노테이션으로 테스트 의도 명확화

## 🚀 Kotlin 개선 제안사항

### 1. Member.kt 개선사항

#### 현재 코드
```kotlin
fun activate() {
    check(this.status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }
    status = MemberStatus.ACTIVE
}

fun deactivate() {
    check(this.status == MemberStatus.ACTIVE) { "ACTIVE 상태가 아닙니다" }
    this.status = MemberStatus.DEACTIVATED
}
```

#### 개선 제안
```kotlin
fun activate() {
    check(status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }
    status = MemberStatus.ACTIVE
}

fun deactivate() {
    check(status == MemberStatus.ACTIVE) { "ACTIVE 상태가 아닙니다" }
    status = MemberStatus.DEACTIVATED
}
```
**이유**: 불필요한 `this.` 참조를 제거하여 일관성 유지

### 2. 데이터 클래스 활용 고려
현재는 단순 클래스지만, 향후 확장을 위해 data class 검토 필요:
```kotlin
data class Member(
    val email: String,
    val nickname: String, 
    val passwordHsh: String
) {
    var status: MemberStatus = MemberStatus.PENDING
        private set
    
    // 메서드들...
}
```
**장점**: `equals()`, `hashCode()`, `toString()` 자동 생성, `status`의 private setter로 캡슐화 강화

### 3. 테스트 코드 Kotest 마이그레이션 제안

#### 현재 코드 (JUnit + AssertJ)
```kotlin
@Test
fun createMember() {
    val member = Member("kskyung0624@gmail.com", "Soko", "secret")
    Assertions.assertThat(member.status).isEqualTo(MemberStatus.PENDING)
}
```

#### Kotest 활용 제안
```kotlin
class MemberTest : StringSpec({
    "회원 생성 시 상태는 PENDING이다" {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")
        member.status shouldBe MemberStatus.PENDING
    }
    
    "PENDING 상태의 회원만 활성화할 수 있다" {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")
        member.activate()
        member.status shouldBe MemberStatus.ACTIVE
    }
    
    "이미 활성화된 회원은 다시 활성화할 수 없다" {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")
        member.activate()
        
        shouldThrow<IllegalStateException> {
            member.activate()
        }
    }
})
```

### 4. 패스워드 타입 개선
```kotlin
@JvmInline
value class Password(val value: String) {
    init {
        require(value.isNotBlank()) { "패스워드는 비어있을 수 없습니다" }
    }
}

class Member(
    val email: String,
    val nickname: String,
    val passwordHash: Password  // 네이밍도 수정
)
```

### 5. 이메일 밸리데이션 추가
```kotlin
@JvmInline  
value class Email(val value: String) {
    init {
        require(value.contains("@")) { "올바른 이메일 형식이 아닙니다" }
    }
}
```

## 📋 우선순위별 적용 제안

### High Priority
1. `this.` 참조 일관성 유지 
2. Kotest 도입 검토

### Medium Priority  
3. Data class 전환 고려
4. Value class를 통한 타입 안전성 강화

### Low Priority
5. 도메인별 밸리데이션 로직 추가

## 🎯 결론
전반적으로 Kotlin의 기본 문법을 잘 활용한 코드입니다. 제안사항들을 단계적으로 적용하면 더욱 Kotlin답고 안전한 코드가 될 것입니다.