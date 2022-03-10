package wscrg.exposeuback.api.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import wscrg.exposeuback.domain.dto.user.UserForm

@SpringBootTest
@AutoConfigureMockMvc
internal class UserApiControllerTest private constructor(
    @Autowired
    val mockMvc: MockMvc,
    @Autowired
    val objectMapper: ObjectMapper
) {
    companion object {
        val log = LoggerFactory.getLogger(MockMvcResultMatchers::class.java)
    }

    @Test
    fun user_and_image_동시_업로드_테스트() {
        //given
        val imageFile = MockMultipartFile(
            "file-data",
            "image.jpeg",
            MediaType.IMAGE_JPEG_VALUE,
            "<<image jpeg>>".toByteArray()
        )

        val content =
            objectMapper.writeValueAsString(
                UserForm(
                    email = "abcdefu@google.com",
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

        //when
        val requestBuilder = multipart("/api/users")
            .file(imageFile)
            .file(json)
            .contentType(MediaType.MULTIPART_MIXED_VALUE)
            .characterEncoding("UTF-8")

        val perform = mockMvc.perform(requestBuilder)

        //then
        val result = perform.andExpect(status().isOk).andReturn()
        val response = result.response
        log.info("response content-type: {}", response.contentType)
        log.info("response content: {}", response.contentAsString)
    }

    @Test
    fun image_미업로드시_400_응답() {
        //given
        //file-data 없음
        val content =
            objectMapper.writeValueAsString(
                UserForm(
                    email = "abcdefu@google.com",
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

        //when
        val requestBuilder = multipart("/api/users")
            .file(json)
            .contentType(MediaType.MULTIPART_MIXED_VALUE)
            .characterEncoding("UTF-8")

        val perform = mockMvc.perform(requestBuilder)

        //then
        val result = perform.andExpect(status().isBadRequest)
    }
}
