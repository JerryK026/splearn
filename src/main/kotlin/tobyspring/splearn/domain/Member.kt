package tobyspring.splearn.domain


class Member(
    val email: String,
    val nickname: String,
    val passwordHsh: String
) {
    var status: MemberStatus = MemberStatus.PENDING

    fun activate() {
        check(this.status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }

        status = MemberStatus.ACTIVE
    }

    fun deactivate() {
        check(this.status == MemberStatus.ACTIVE) { "ACTIVE 상태가 아닙니다" }

        this.status = MemberStatus.DEACTIVATED
    }
}
