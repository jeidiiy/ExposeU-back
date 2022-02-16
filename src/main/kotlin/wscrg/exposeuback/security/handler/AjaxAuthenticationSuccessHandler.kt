package wscrg.exposeuback.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import wscrg.exposeuback.domain.dto.authentication.UserDto
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AjaxAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    private val mapper = ObjectMapper()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val userDto = authentication?.principal as UserDto

        response?.status = HttpStatus.OK.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE

        mapper.writeValue(response?.writer, userDto)
    }
}