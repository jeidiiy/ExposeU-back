package wscrg.exposeuback.repository

import org.springframework.data.jpa.repository.JpaRepository
import wscrg.exposeuback.domain.entity.Like
import wscrg.exposeuback.domain.entity.UploadFile
import wscrg.exposeuback.domain.entity.User
import java.util.*

interface LikeRepository: JpaRepository<Like, Long> {

    fun findByUserAndImage(user: User, image: UploadFile): Optional<Like>
}
