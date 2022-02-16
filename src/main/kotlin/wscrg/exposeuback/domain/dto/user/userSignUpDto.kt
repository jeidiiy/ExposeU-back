package wscrg.exposeuback.domain.dto.user

import wscrg.exposeuback.domain.entity.Image
import wscrg.exposeuback.domain.entity.User

data class UserSignUpRequestDto(
    var email: String,
    var password: String,
    var name: String,
    var image: Image?
)

data class UserSignUpResponseDto(
    var id: Long,
    var email: String,
) {
    companion object {
        fun of(user: User) = UserSignUpResponseDto(user.id ?: -1, user.email)
    }
}