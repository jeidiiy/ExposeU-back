package wscrg.exposeuback.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxAuthenticationFailureHandler : AuthenticationFailureHandler {

    private val mapper = ObjectMapper()

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        var errorMessage = "Invalid Username or Password"

        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE

        when (exception) {
            is BadCredentialsException -> errorMessage = "Invalid Username or Password"
            is DisabledException -> errorMessage = "Locked"
            is CredentialsExpiredException -> errorMessage = "Expired password"
        }

        mapper.writeValue(response?.writer, errorMessage)
    }
}