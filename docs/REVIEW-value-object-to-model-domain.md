# Code Review: feat/value-object-to-model-domain

## 🚀 Kotlin 개선 제안사항

### 1. Email Value Object 패턴 개선
```kotlin
// 개선: @JvmInline value class 사용으로 메모리 효율성 향상
@JvmInline
value class Email(val address: String) {
    init {
        require(isValidEmail(address)) { "이메일 형식이 바르지 않습니다: $address" }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}
```

### 2. 정규식 패턴 개선
```kotlin
// 개선: Regex 클래스 사용 + 간소화된 패턴
data class Email(val address: String) {
    companion object {
        private val EMAIL_REGEX = Regex("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")
    }
    
    init {
        require(EMAIL_REGEX.matches(address)) { "이메일 형식이 바르지 않습니다: $address" }
    }
}
```

### 3. 테스트 코드 개선
```kotlin
// 개선: 더 다양한 테스트 케이스 추가
class EmailTest: StringSpec({
    "유효한 이메일 검증" {
        Email("test@gmail.com").address shouldBe "test@gmail.com"
        Email("user.name@domain.co.kr").address shouldBe "user.name@domain.co.kr"
    }
    
    "잘못된 이메일 형식 예외 테스트" {
        shouldThrow<IllegalArgumentException> { Email("invalid") }
        shouldThrow<IllegalArgumentException> { Email("@domain.com") }
        shouldThrow<IllegalArgumentException> { Email("user@") }
    }
    
    "동등성 비교 테스트" {
        Email("test@gmail.com") shouldBe Email("test@gmail.com")
    }
})
```

### 4. Member 생성자 패턴 개선
```kotlin
// 개선: Primary constructor 활용
class Member private constructor(
    val email: Email,
    var nickname: String,
    private var passwordHsh: String
) {
    var status: MemberStatus = MemberStatus.PENDING
        private set
        
    companion object {
        fun create(createRequest: MemberCreateRequest, passwordEncoder: PasswordEncoder): Member {
            return Member(
                email = Email(createRequest.email),
                nickname = createRequest.nickname,
                passwordHsh = passwordEncoder.encode(createRequest.password)
            )
        }
    }
}
```

### 5. 불필요한 코드 제거
```kotlin
// 개선: 이미 기본값이 PENDING이므로 재설정 불필요
companion object {
    fun create(createRequest: MemberCreateRequest, passwordEncoder: PasswordEncoder): Member {
        return Member(
            email = Email(createRequest.email),
            nickname = createRequest.nickname,
            passwordHsh = passwordEncoder.encode(createRequest.password)
        )
        // member.status = MemberStatus.PENDING 제거
    }
}
```

### 6. 파일 끝 newline 추가
```kotlin
// EmailTest.kt 파일 끝에 newline 추가 필요
```

## 📋 우선순위

### High Priority
1. @JvmInline value class 패턴으로 Email 최적화
2. Primary constructor로 Member 구조 개선
3. 파일 끝 newline 추가

### Medium Priority
4. Regex 클래스 사용으로 정규식 최적화
5. 더 포괄적인 Email 테스트 케이스 추가
6. 불필요한 status 재설정 제거