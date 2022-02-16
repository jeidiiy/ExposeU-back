package wscrg.exposeuback.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import wscrg.exposeuback.domain.dto.user.UserSignUpRequestDto
import wscrg.exposeuback.domain.dto.user.UserSignUpResponseDto
import wscrg.exposeuback.domain.entity.User
import wscrg.exposeuback.service.UserService

@RequestMapping("/api/users")
@RestController
class UserApiController(
    val userService: UserService
) {

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long) = userService.findById(id)

    @PostMapping
    fun signUp(@RequestBody requestDto: UserSignUpRequestDto): ResponseEntity<UserSignUpResponseDto> {
        val savedUser = userService.save(User.of(requestDto))
        return ResponseEntity.ok(UserSignUpResponseDto.of(savedUser))
    }
}