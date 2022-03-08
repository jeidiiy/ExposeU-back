package wscrg.exposeuback.domain.dto.user

import wscrg.exposeuback.domain.entity.UploadFile
import wscrg.exposeuback.domain.entity.User

data class UserForm(
    var email: String,
    var password: String,
    var username: String,
)

data class UserSignUpRequestDto(
    var email: String,
    var password: String,
    var username: String,
    var image: UploadFile?
)

data class UserSignUpResponseDto(
    var id: Long,
    var email: String,
) {
    companion object {
        fun of(user: User) = UserSignUpResponseDto(user.id ?: -1, user.email)
    }
}