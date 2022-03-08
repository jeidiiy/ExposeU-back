package wscrg.exposeuback.domain.dto.user

data class UserLoginRequestDto(
    val email: String,
    val password: String,
    val jsessionId: String?
)

data class UserLoginResponseDto(
    val id: Long,
    val email: String,
)