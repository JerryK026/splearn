package tobyspring.splearn.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MemberKotestTest : StringSpec({
    "회원 생성 시 상태는 PENDING이다" {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")
        member.status shouldBe MemberStatus.PENDING
    }

    "PENDING 상태의 회원만 활성화할 수 있다" {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")
        member.activate()
        member.status shouldBe MemberStatus.ACTIVE

        shouldThrow<IllegalStateException> {
            member.activate()
        }
    }

    "비활성화 실패 테스트" {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")

        shouldThrow<IllegalStateException> {
            member.deactivate()
        }

        member.activate()

        shouldThrow<IllegalStateException> {
            member.activate()
        }
    }
})
