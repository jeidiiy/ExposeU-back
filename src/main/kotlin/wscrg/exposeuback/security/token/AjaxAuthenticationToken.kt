package wscrg.exposeuback.security.token

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import wscrg.exposeuback.domain.dto.authentication.UserDto
import wscrg.exposeuback.domain.dto.user.UserLoginRequestDto

class AjaxAuthenticationToken : AbstractAuthenticationToken {

    private val principal: Any
        @JvmName("getPrincipalKt") get

    private val credentials: Any
        @JvmName("getPasswordKt") get

    constructor(principal: Any, credentials: Any) : super(null) {
        this.principal = principal
        this.credentials = credentials
        isAuthenticated = false
    }

    constructor(
        principal: Any,
        credentials: Any,
        authorities: Collection<GrantedAuthority>
    ) : super(authorities) {
        this.principal = principal
        this.credentials = credentials
        isAuthenticated = true
    }

    companion object {
        fun of(userDto: UserLoginRequestDto) = AjaxAuthenticationToken(userDto, userDto.password)
        fun of(userDto: UserDto) = AjaxAuthenticationToken(userDto, userDto.password)
    }

    override fun getCredentials(): Any {
        return credentials
    }

    override fun getPrincipal(): Any {
        return principal
    }
}