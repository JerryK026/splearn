# Code Review: feat/improve-domain-model-code

## 🚀 Kotlin 개선 제안사항

### 1. Member 생성자 패턴 개선
```kotlin
// 현재: lateinit var 사용
class Member private constructor() {
    lateinit var email: String
    lateinit var nickname: String
    lateinit var passwordHsh: String
    
    companion object {
        fun create(createRequest: MemberCreateRequest, passwordEncoder: PasswordEncoder): Member {
            val member = Member()
            member.email = createRequest.email
            member.nickname = createRequest.nickname
            member.passwordHsh = passwordEncoder.encode(createRequest.password)
            member.status = MemberStatus.PENDING
            return member
        }
    }
}

// 개선: Primary constructor 활용
class Member private constructor(
    val email: String,
    var nickname: String,
    private var passwordHsh: String
) {
    var status: MemberStatus = MemberStatus.PENDING
        private set
        
    companion object {
        fun create(createRequest: MemberCreateRequest, passwordEncoder: PasswordEncoder): Member {
            return Member(
                email = createRequest.email,
                nickname = createRequest.nickname, 
                passwordHsh = passwordEncoder.encode(createRequest.password)
            )
        }
    }
}
```

### 2. 불변성 강화
```kotlin
// 현재: email도 mutable
lateinit var email: String

// 개선: email은 불변으로
val email: String
```

### 3. 코드 일관성 개선
```kotlin
// 현재: this. 참조 불일치 + 불필요한 Unit
fun activate(): Unit {
    check(this.status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }
    this.status = MemberStatus.ACTIVE
}

// 개선: 일관된 스타일
fun activate() {
    check(status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }
    status = MemberStatus.ACTIVE
}
```

### 4. MemberCreateRequest 개선
```kotlin
// 현재: 파일 끝에 newline 없음
data class MemberCreateRequest(val email: String, val nickname: String, val password: String)

// 개선: newline 추가 + 검증 로직
data class MemberCreateRequest(
    val email: String,
    val nickname: String,
    val password: String
) {
    init {
        require(email.isNotBlank()) { "이메일은 필수입니다" }
        require(nickname.isNotBlank()) { "닉네임은 필수입니다" }
        require(password.isNotBlank()) { "비밀번호는 필수입니다" }
    }
}
```

### 5. 불필요한 상태 설정 제거
```kotlin
// 현재: 이미 초기값이 PENDING인데 다시 설정
member.status = MemberStatus.PENDING

// 개선: 불필요한 설정 제거 (이미 기본값이 PENDING)
```

## 📋 우선순위

### High Priority
1. Primary constructor 패턴으로 복원 (lateinit var → constructor parameter)
2. 불필요한 `Unit` 제거 및 `this.` 일관성
3. MemberCreateRequest에 newline 추가

### Medium Priority
4. email 불변성 확보 (val로 변경)
5. MemberCreateRequest 검증 로직 추가
6. 불필요한 status 설정 제거