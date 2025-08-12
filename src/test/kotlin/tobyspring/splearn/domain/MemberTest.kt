package tobyspring.splearn.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MemberTest : StringSpec({
    lateinit var member: Member
    lateinit var passwordEncoder: PasswordEncoder

    beforeEach {
        passwordEncoder = object : PasswordEncoder {
            override fun encode(password: String): String {
                return password.uppercase()
            }

            override fun matches(password: String, passwordHash: String): Boolean {
                return encode(password) == passwordHash
            }
        }
        member = Member.create(MemberCreateRequest("kskyung0624@gmail.com", "Soko", "secret"), passwordEncoder)
    }

    "회원 생성 시 상태는 PENDING이다" {
        member.status shouldBe MemberStatus.PENDING
    }

    "PENDING 상태의 회원만 활성화할 수 있다" {
        member.activate()
        member.status shouldBe MemberStatus.ACTIVE

        shouldThrow<IllegalStateException> {
            member.activate()
        }
    }

    "비활성화 실패 테스트" {
        shouldThrow<IllegalStateException> {
            member.deactivate()
        }

        member.activate()

        shouldThrow<IllegalStateException> {
            member.activate()
        }
    }

    "비밀번호 검증 테스트" {
        member.verifyPassword("secret", passwordEncoder) shouldBe true
        member.verifyPassword("hello", passwordEncoder) shouldBe false
    }

    "닉네임 변경 테스트" {
        member.nickname shouldBe "Soko"
        member.changeNickname("Bomi")
        member.nickname shouldBe "Bomi"
    }

    "비밀번호 변경 테스트" {
        member.changePassword("verySecret", passwordEncoder)

        member.verifyPassword("verySecret", passwordEncoder) shouldBe true
    }

    "active 상태 검증 테스트" {
        member.isActive() shouldBe false

        member.activate()
        member.isActive() shouldBe true

        member.deactivate()
        member.isActive() shouldBe false
    }

    "불필요한 이메일 입력시 예외 발생 테스트" {
        shouldThrow<IllegalArgumentException> {
            Member.create(MemberCreateRequest("invalid emain", "Soko", "secret"), passwordEncoder)
        }

        Member.create(MemberCreateRequest("kskyung0624@gmail.com", "Soko", "secret"), passwordEncoder)
    }
})
