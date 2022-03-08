package wscrg.exposeuback.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import wscrg.exposeuback.domain.entity.UploadFile
import java.io.File
import java.util.UUID

@Component
class FileUtil(
    @Value("\${file.dir}")
    val fileDir: String
) {
    fun storeFile(multipartFile: MultipartFile?): UploadFile? {
        if (multipartFile === null) return null

        val originalFilename = multipartFile.originalFilename ?: "Default Image Name.png"
        val storeFilename = createStoreFilename(originalFilename)
        multipartFile.transferTo(File(getFullPath(storeFilename)))
        return UploadFile(originalFilename, storeFilename)
    }

    fun getFullPath(fileName: String): String = "${fileDir}${fileName}"

    fun createStoreFilename(originalFilename: String): String {
        val uuid = UUID.randomUUID().toString()
        val ext = extractExt(originalFilename)
        return "${uuid}.${ext}"
    }

    private fun extractExt(originalFilename: String): String {
        val pos = originalFilename.lastIndexOf(".")
        return originalFilename.substring(pos + 1)
    }
}
