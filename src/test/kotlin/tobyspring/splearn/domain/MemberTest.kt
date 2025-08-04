package tobyspring.splearn.domain

import org.assertj.core.api.Assertions
import kotlin.test.Test

class MemberTest {
    @Test
    fun createMember() {
        val member = Member("kskyung0624@gmail.com", "Soko", "secret")

        Assertions.assertThat(member.status).isEqualTo(MemberStatus.PENDING)
    }
}
