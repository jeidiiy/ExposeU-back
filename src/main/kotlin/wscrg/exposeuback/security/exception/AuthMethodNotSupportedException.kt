package wscrg.exposeuback.security.exception

import org.springframework.security.authentication.AuthenticationServiceException

class AuthMethodNotSupportedException(
    message: String
) : AuthenticationServiceException(message)