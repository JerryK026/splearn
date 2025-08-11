package tobyspring.splearn.domain


class Member private constructor() {
    lateinit var email: String
    lateinit var nickname: String
    lateinit var passwordHsh: String
    var status: MemberStatus = MemberStatus.PENDING
        private set

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

    fun activate(): Unit {
        check(this.status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }

        this.status = MemberStatus.ACTIVE
    }

    fun deactivate(): Unit {
        check(this.status == MemberStatus.ACTIVE) { "ACTIVE 상태가 아닙니다" }

        this.status = MemberStatus.DEACTIVATED
    }

    fun verifyPassword(password: String, passwordEncoder: PasswordEncoder): Boolean {
        return passwordEncoder.matches(password, this.passwordHsh)
    }

    fun changeNickname(nickname: String) {
        this.nickname = nickname
    }

    fun changePassword(password: String, passwordEncoder: PasswordEncoder) {
        this.passwordHsh = passwordEncoder.encode(password)
    }

    fun isActive(): Boolean {
        return this.status == MemberStatus.ACTIVE
    }
}
