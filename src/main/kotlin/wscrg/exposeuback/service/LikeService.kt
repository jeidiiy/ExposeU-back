package wscrg.exposeuback.service

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import wscrg.exposeuback.domain.dto.authentication.UserDto
import wscrg.exposeuback.domain.entity.Like
import wscrg.exposeuback.domain.entity.UploadFile
import wscrg.exposeuback.domain.entity.User
import wscrg.exposeuback.repository.LikeRepository
import wscrg.exposeuback.repository.UploadFileRepository
import wscrg.exposeuback.repository.UserRepository

@Service
class LikeService(
    val likeRepository: LikeRepository,
    val userRepository: UserRepository,
    val uploadFileRepository: UploadFileRepository
) {
    fun addLike(userDto: UserDto, imageId: Long): Boolean {
        val uploadFile = uploadFileRepository.findById(imageId).orElseThrow()

        val user = userRepository.findByEmail(userDto.getEmail())
            ?: throw UsernameNotFoundException("No user with this email: ${userDto.getEmail()}")

        return if (!isAlreadyLike(user, uploadFile)) {
            likeRepository.save(Like(user = user, image = uploadFile))
            true
        } else false
    }

    fun subLike(userDto: UserDto, imageId: Long): Boolean {
        val uploadFile = uploadFileRepository.findById(imageId).orElseThrow()

        val user = userRepository.findByEmail(userDto.getEmail())
            ?: throw UsernameNotFoundException("No user with this email: ${userDto.getEmail()}")

        return if (isAlreadyLike(user, uploadFile)) {
            likeRepository.delete(likeRepository.findByUserAndImage(user, uploadFile).get())
            true
        } else false
    }

    private fun isAlreadyLike(user: User, image: UploadFile): Boolean =
        likeRepository.findByUserAndImage(user, image).isPresent
}
