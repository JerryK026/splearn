package tobyspring.splearn.domain

import java.util.regex.Pattern

data class Email(val address: String) {
    companion object {
        val EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+(?:\\.[a-zA-Z0-9._%+-]+)*@(?:[a-zA-Z0-9.-]+\\.)+[a-zA-Z]{2,7}$")
    }
    init {
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw IllegalArgumentException("이메일 형식이 바르지 않습니다: $address")
        }
    }
}
