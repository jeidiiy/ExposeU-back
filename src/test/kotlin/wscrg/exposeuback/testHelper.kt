package wscrg.exposeuback

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import wscrg.exposeuback.domain.dto.user.UserForm
import java.util.*

val objectMapper = ObjectMapper().registerKotlinModule()

fun createMockUser(hasImage: Boolean): MockHttpServletRequestBuilder {
    val imageFile = if (hasImage)
        MockMultipartFile(
            "file-data",
            "image.jpeg",
            MediaType.IMAGE_JPEG_VALUE,
            "<<image jpeg>>".toByteArray()
        ) else null

    var random = UUID.randomUUID().toString()
    random = random.substring(random.lastIndexOf("-") + 1)

    val content =
        objectMapper.writeValueAsString(
            UserForm(
                email = "${random}@google.com",
                username = "jiji",
                password = "1234"
            )
        )

    val json = MockMultipartFile(
        "user-data",
        "jsonData",
        MediaType.APPLICATION_JSON_VALUE,
        content.toByteArray()
    )

    return when (hasImage) {
        true -> MockMvcRequestBuilders.multipart("/api/users")
            .file(imageFile!!)
            .file(json)
            .contentType(MediaType.MULTIPART_MIXED_VALUE)
            .characterEncoding("UTF-8")
        false -> MockMvcRequestBuilders.multipart("/api/users")
            .file(json)
            .contentType(MediaType.MULTIPART_MIXED_VALUE)
            .characterEncoding("UTF-8")
    }
}
