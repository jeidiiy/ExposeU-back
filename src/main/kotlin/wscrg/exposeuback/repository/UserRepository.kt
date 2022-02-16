package wscrg.exposeuback.repository

import org.springframework.data.jpa.repository.JpaRepository
import wscrg.exposeuback.domain.entity.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(name: String): User?
}