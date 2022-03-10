package wscrg.exposeuback.domain.dto.error

import org.springframework.http.HttpStatus

data class ErrorResult(
    var code: HttpStatus? = HttpStatus.OK,
    var message: String? = "Default Message"
)