package tobyspring.splearn.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EmailTest: StringSpec({
    "동등성 비교 테스트" {
        val email1 = Email("kskyung0624@gmail.com")
        val email2 = Email("kskyung0624@gmail.com")

        email1 shouldBe email2
    }
})
