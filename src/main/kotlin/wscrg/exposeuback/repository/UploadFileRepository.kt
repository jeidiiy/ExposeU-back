package wscrg.exposeuback.repository

import org.springframework.data.jpa.repository.JpaRepository
import wscrg.exposeuback.domain.entity.UploadFile

interface UploadFileRepository: JpaRepository<UploadFile, Long>
