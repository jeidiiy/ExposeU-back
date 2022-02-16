package wscrg.exposeuback.domain.dto.authentication

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable

class UserDto(
    private val email: String,
    private val password: String
) : Serializable, UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        //TODO: 권한 관련 설계 후 작성
        return mutableListOf()
    }

    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    class Builder {

        private var email: String = ""
        private var password: String = ""

        fun email(email: String) = apply {
            this.email = email
        }

        fun password(password: String) = apply {
            this.password = password
        }

        fun build() = UserDto(email, password)
    }
}