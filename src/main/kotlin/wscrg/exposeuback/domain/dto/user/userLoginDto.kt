package wscrg.exposeuback.domain.dto.user

data class UserLoginRequestDto(
    val email: String,
    val password: String
)

data class UserLoginResponseDto(
    val id: Long,
    val email: String,
)