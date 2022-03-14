package wscrg.exposeuback.api.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import wscrg.exposeuback.domain.dto.user.UserForm
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class UserApiControllerTest private constructor(
    @Autowired
    val mockMvc: MockMvc,
) {
    companion object {
        val log = LoggerFactory.getLogger(MockMvcResultMatchers::class.java)
    }

    val objectMapper = ObjectMapper().registerKotlinModule()

    @Test
    fun user_and_image_동시_업로드_테스트() {
        //given
        val requestBuilder = createMockUser(hasImage = true)

        //when
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
        val requestBuilder = createMockUser(hasImage = false)

        //when
        val perform = mockMvc.perform(requestBuilder)

        //then
        perform.andExpect(status().isBadRequest)
    }

    @Test
    fun 회원조회() {
        //given
        val requestBuilder = createMockUser(hasImage = true)

        val perform = mockMvc.perform(requestBuilder)
        val mvcResult = perform.andReturn()
        val userResponseDto = objectMapper.readValue(mvcResult.response.contentAsString, UserResponseDto::class.java)

        //when
        val resultActions = mockMvc.perform(
            get("/api/users/${userResponseDto.id}")
        )

        //then
        resultActions.andExpect(status().isOk)
    }

    @Test
    fun 회원탈퇴() {
        //given
        val requestBuilder = createMockUser(hasImage = true)

        val perform = mockMvc.perform(requestBuilder)
        val mvcResult = perform.andReturn()
        val userResponseDto = objectMapper.readValue(mvcResult.response.contentAsString, UserResponseDto::class.java)

        val jsonData = HashMap<String, Long>()
        jsonData["id"] = userResponseDto.id

        //when
        val resultActions = mockMvc.perform(
            delete("/api/users/${userResponseDto.id}")
        )

        //then
        resultActions.andExpect(status().isOk)
    }

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
            true -> multipart("/api/users")
                .file(imageFile!!)
                .file(json)
                .contentType(MediaType.MULTIPART_MIXED_VALUE)
                .characterEncoding("UTF-8")
            false -> multipart("/api/users")
                .file(json)
                .contentType(MediaType.MULTIPART_MIXED_VALUE)
                .characterEncoding("UTF-8")
        }
    }

    data class UserResponseDto(
        var id: Long,
        var email: String
    )
}
