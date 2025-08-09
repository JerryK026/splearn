package tobyspring.splearn.domain


class Member private constructor(
    val email: String,
    var nickname: String,
    var passwordHsh: String
) {
    var status: MemberStatus = MemberStatus.PENDING

    companion object {
        fun create(email: String, nickname: String, password: String, passwordEncoder: PasswordEncoder): Member {
            return Member(email, nickname, passwordEncoder.encode(password))
        }
    }

    fun activate(): Unit {
        check(this.status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }

        status = MemberStatus.ACTIVE
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
}
