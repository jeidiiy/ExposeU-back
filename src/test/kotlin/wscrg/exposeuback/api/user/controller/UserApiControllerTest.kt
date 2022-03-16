package wscrg.exposeuback.api.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import wscrg.exposeuback.createMockUser

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class UserApiControllerTest private constructor(
    @Autowired val mockMvc: MockMvc,
) {
    val objectMapper = ObjectMapper().registerKotlinModule()

    @Test
    @DisplayName("유저 및 이미지 정보를 multipart/form-data로 함께 제출")
    fun signUp() {
        //given
        val requestBuilder = createMockUser(hasImage = true)

        //when
        mockMvc.perform(requestBuilder)
            //then
            .andExpect(status().isCreated).andReturn()
    }

    @Test
    @DisplayName("이미지 미업로드 시 응답코드 400")
    fun imageIsNotUploaded() {
        //given
        //file-data 없음
        val requestBuilder = createMockUser(hasImage = false)

        //when
        mockMvc.perform(requestBuilder)
            //then
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("회원조회")
    fun selectUser() {
        //given
        val requestBuilder = createMockUser(hasImage = true)

        val perform = mockMvc.perform(requestBuilder)
        val mvcResult = perform.andReturn()
        val userResponseDto = objectMapper.readValue(mvcResult.response.contentAsString, UserResponseDto::class.java)

        //when
        mockMvc.perform(
            get("/api/users/${userResponseDto.id}")
        )
            //then
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("회원탈퇴")
    fun dropOut() {
        //given
        val requestBuilder = createMockUser(hasImage = true)

        val perform = mockMvc.perform(requestBuilder)
        val mvcResult = perform.andReturn()
        val userResponseDto = objectMapper.readValue(mvcResult.response.contentAsString, UserResponseDto::class.java)

        val jsonData = HashMap<String, Long>()
        jsonData["id"] = userResponseDto.id

        //when
        mockMvc.perform(
            delete("/api/users/${userResponseDto.id}")
        )
            //then
            .andExpect(status().isNoContent)
    }

    data class UserResponseDto(
        var id: Long,
        var email: String
    )
}
