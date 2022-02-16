package wscrg.exposeuback.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.util.StringUtils
import wscrg.exposeuback.domain.dto.user.UserLoginRequestDto
import wscrg.exposeuback.security.exception.AuthMethodNotSupportedException
import wscrg.exposeuback.security.token.AjaxAuthenticationToken
import wscrg.exposeuback.util.isAjax
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxLoginProcessingFilter : AbstractAuthenticationProcessingFilter(
    AntPathRequestMatcher("/api/login")
) {
    private val objectMapper = ObjectMapper().registerKotlinModule()

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (HttpMethod.POST.name != request?.method || !isAjax(request)) {
            throw AuthMethodNotSupportedException("Authentication method not supported.")
        }

        val requestDto = objectMapper.readValue(request.reader, UserLoginRequestDto::class.java)
        if (!StringUtils.hasLength(requestDto.email) || !StringUtils.hasLength(requestDto.password)) {
            throw AuthenticationServiceException("Username or Password ot provided")
        }

        val ajaxAuthenticationToken = AjaxAuthenticationToken.of(requestDto)

        return this.authenticationManager.authenticate(ajaxAuthenticationToken)
    }
}