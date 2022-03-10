package wscrg.exposeuback.api.user.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import wscrg.exposeuback.api.user.controller.UserApiController
import wscrg.exposeuback.domain.dto.error.ErrorResult

@RestControllerAdvice("wscrg.exposeuback.api")
class UserApiControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    fun handleBadRequest(ex: IllegalArgumentException) = ErrorResult(BAD_REQUEST, ex.message)

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    fun handleServerError(ex: Exception) = ErrorResult(INTERNAL_SERVER_ERROR, ex.message)
}
