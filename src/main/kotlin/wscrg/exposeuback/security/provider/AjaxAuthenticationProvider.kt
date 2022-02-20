package wscrg.exposeuback.security.provider

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import wscrg.exposeuback.domain.dto.authentication.UserDto
import wscrg.exposeuback.domain.dto.user.UserLoginRequestDto
import wscrg.exposeuback.security.token.AjaxAuthenticationToken

@Component
class AjaxAuthenticationProvider(
    val userDetailsService: UserDetailsService,
    val passwordEncoder: PasswordEncoder
) : AuthenticationProvider {
    companion object {
        val log: Logger = LoggerFactory.getLogger(AjaxAuthenticationProvider::class.java)
    }

    override fun authenticate(authentication: Authentication): Authentication {

        val (email, password) = authentication.principal as UserLoginRequestDto
        val userDetails: UserDetails?

        try {
            userDetails = userDetailsService.loadUserByUsername(email)

            if (userDetails === null || !passwordEncoder.matches(password, userDetails.password)) {
                throw BadCredentialsException("Invalid password")
            }

            if (!userDetails.isEnabled) {
                throw BadCredentialsException("This user is not confirmed.")
            }
        } catch (e: Exception) {
            log.info(e.toString())
            when (e) {
                is UsernameNotFoundException -> throw UsernameNotFoundException(e.message)
                is BadCredentialsException -> throw BadCredentialsException(e.message)
                is RuntimeException -> throw RuntimeException(e.message)
            }
        }

        val userDto = UserDto.Builder().email(email).password(password).build()
        return AjaxAuthenticationToken(userDto, userDto.password, userDto.authorities)
    }

    override fun supports(authentication: Class<*>?): Boolean =
        authentication == AjaxAuthenticationToken::class.java
}