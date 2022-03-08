package wscrg.exposeuback.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import wscrg.exposeuback.domain.entity.UploadFile
import wscrg.exposeuback.repository.UploadFileRepository

@Service
class UploadFileService(
    val uploadFileRepository: UploadFileRepository
) {

    @Transactional
    fun save(file: UploadFile): Long {
        val uploadFile = uploadFileRepository.save(file)
        return uploadFile.id!!
    }
}
