package wscrg.exposeuback.domain.dto.user

import wscrg.exposeuback.domain.entity.UploadFile

data class UserResponseDto(
    var id: Long,
    var email: String,
    var username: String,
    var image: UploadFile?
)