package wscrg.exposeuback.api.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import wscrg.exposeuback.createMockUser
import wscrg.exposeuback.domain.dto.user.UserLoginRequestDto
import wscrg.exposeuback.repository.LikeRepository
import wscrg.exposeuback.repository.UserRepository

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
internal class LikeApiControllerTest private constructor(
    @Autowired val mockMvc: MockMvc,
    @Autowired val userRepository: UserRepository,
    @Autowired val likeRepository: LikeRepository,
) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(LikeApiControllerTest::class.java)
    }

    val objectMapper = ObjectMapper().registerKotlinModule()

    @Test
    @DisplayName("좋아요")
    @WithUserDetails("abcdefu@gmail.com")
    fun createLike() {
        //given
        val requestBuilder = createMockUser(hasImage = true)
        val perform = mockMvc.perform(requestBuilder)

        val result = perform.andReturn()
        val userResponseDto =
            objectMapper.readValue(result.response.contentAsString, UserApiControllerTest.UserResponseDto::class.java)

        //when
        //로그인
        val loginUserDto = UserLoginRequestDto("abcdefu@gmail.com", password = "1234")

        mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginUserDto))
        )

        val newUser = userRepository.findById(userResponseDto.id).orElseThrow()

        mockMvc.perform(
            post("/api/like/${newUser.image?.id}")
        )

            //then
            .andExpect(status().isOk)

        val like = likeRepository.findAll()[0]
        assertThat(like).isNotNull
        log.info("Like: {}", like)
        assertThat(like.user.id).isNotNull
        log.info("좋아요를 누른 유저의 id: {}", like.user.id)
        assertThat(like.image.id).isNotNull
        log.info("좋아요가 눌린 이미지의 id: {}", like.image.id)
    }
}
