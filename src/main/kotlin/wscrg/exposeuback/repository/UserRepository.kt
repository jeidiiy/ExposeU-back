package wscrg.exposeuback.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import wscrg.exposeuback.domain.entity.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(name: String): User?

    @Query("select u from User u join fetch u.image where u.id = :id")
    fun findByIdOrNull(@Param("id") id: Long): User?
}