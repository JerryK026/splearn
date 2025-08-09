# Code Review: feat/extend-member-domain

## 🚀 Kotlin 개선 제안사항

### 1. 코드 일관성 개선
```kotlin
// 현재: this. 참조 불일치
fun activate(): Unit {
    check(this.status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }
    status = MemberStatus.ACTIVE
}

// 개선: this. 참조 일관성 유지 + 불필요한 Unit 제거
fun activate() {
    check(status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }
    status = MemberStatus.ACTIVE
}
```

### 2. 테스트 코드 오타 및 로직 수정
```kotlin
// 현재: 변수명 오타
lateinit var passowrdEncoder: PasswordEncoder

// 개선: 오타 수정
lateinit var passwordEncoder: PasswordEncoder

// 현재: 테스트 로직 오류
"비밀번호 변경 테스트" {
    member.changePassword("verySecret", passowrdEncoder)
    member.verifyPassword("secret", passowrdEncoder) shouldBe true  // 잘못된 검증
}

// 개선: 올바른 검증 로직
"비밀번호 변경 테스트" {
    member.changePassword("verySecret", passwordEncoder)
    member.verifyPassword("verySecret", passwordEncoder) shouldBe true
    member.verifyPassword("secret", passwordEncoder) shouldBe false
}
```

### 3. PasswordEncoder를 fun interface로 변경
```kotlin
fun interface PasswordEncoder {
    fun encode(password: String): String
    
    fun matches(password: String, passwordHash: String): Boolean {
        return encode(password) == passwordHash
    }
}

// 테스트에서 람다로 간소화 가능
passwordEncoder = PasswordEncoder { it.uppercase() }
```

### 4. 캡슐화 강화
```kotlin
class Member private constructor(
    val email: String,
    var nickname: String,
    private var passwordHsh: String  // private으로 변경
) {
    var status: MemberStatus = MemberStatus.PENDING
        private set  // setter를 private으로
}
```

### 5. Value Object 도입 (선택사항)
```kotlin
@JvmInline
value class Email(val value: String) {
    init {
        require(value.contains("@")) { "유효하지 않은 이메일 형식" }
    }
}
```

## 📋 우선순위

### High Priority
1. 변수명 오타 수정 (`passowrdEncoder` → `passwordEncoder`)
2. 테스트 로직 수정 (비밀번호 변경 검증)
3. `this.` 참조 일관성 + `Unit` 제거

### Medium Priority
4. `fun interface` 도입
5. private setter로 캡슐화 강화