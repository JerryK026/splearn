package tobyspring.splearn.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test

class MemberTest {
    @Test
    fun createMember() {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")

        Assertions.assertThat(member.status).isEqualTo(MemberStatus.PENDING)
    }

    // 컴파일 에러 발생해서 주석 처리
    //    @Test
    //    fun constructorNullCheck() {
    //        Assertions.assertThatThrownBy {
    //            Member(null, "Soko", "secret")
    //        }.isInstanceOf(NullPointerException::class.java)
    //    }

    @Test
    fun activate() {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")

        member.activate()

        Assertions.assertThat(member.status).isEqualTo(MemberStatus.ACTIVE)
    }

    @Test
    fun activateFail() {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")

        member.activate()

        Assertions.assertThatThrownBy { member.activate() }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    @DisplayName("ACTIVE 상태에서만 DEACTIVATE 상태로 만들 수 있다.")
    fun deactivate() {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")
        member.activate()

        member.deactivate()

        Assertions.assertThat(member.status).isEqualTo(MemberStatus.DEACTIVATED)
    }

    @Test
    fun deactivateFail() {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")

        Assertions.assertThatThrownBy { member.deactivate() }
            .isInstanceOf(IllegalStateException::class.java)

        member.activate()
        member.deactivate()

        Assertions.assertThatThrownBy { member.deactivate() }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
