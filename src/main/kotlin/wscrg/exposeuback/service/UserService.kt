package wscrg.exposeuback.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import wscrg.exposeuback.domain.entity.User
import wscrg.exposeuback.repository.UserRepository

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun findById(id: Long): User =
        userRepository.findByIdOrNull(id)
            ?: throw UsernameNotFoundException(String.format("No user was founded. id: %s", id))

    @Transactional
    fun save(user: User): User = userRepository.save(user)

    fun delete(id: Long) = userRepository.deleteById(id)

}