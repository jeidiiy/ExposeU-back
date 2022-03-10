package wscrg.exposeuback.api.user.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import wscrg.exposeuback.domain.dto.authentication.UserDto
import wscrg.exposeuback.domain.entity.UploadFile
import wscrg.exposeuback.domain.dto.user.UserForm
import wscrg.exposeuback.domain.dto.user.UserSignUpRequestDto
import wscrg.exposeuback.domain.dto.user.UserSignUpResponseDto
import wscrg.exposeuback.service.UploadFileService
import wscrg.exposeuback.service.UserService
import wscrg.exposeuback.util.FileUtil
import javax.servlet.http.HttpServletRequest

@RequestMapping("/api/users")
@RestController
class UserApiController(
    val userService: UserService,
    val uploadFileService: UploadFileService,
    val fileUtil: FileUtil
) {
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long) = userService.findById(id)

    @PostMapping
    fun signUp(
        @RequestPart("user-data") userForm: UserForm,
        @RequestPart("file-data") image: MultipartFile
    ): ResponseEntity<UserSignUpResponseDto> {
        val storeFile: UploadFile =
            fileUtil.storeFile(image) ?: throw IllegalStateException("Image is not submitted.")

        uploadFileService.save(storeFile)

        val userRequestDto = with(userForm) {
            UserSignUpRequestDto(email = email, username = username, password = password, image = storeFile)
        }

        val savedUser = userService.save(userRequestDto)
        return ResponseEntity.ok(UserSignUpResponseDto.of(savedUser))
    }

    @GetMapping("/authentication")
    fun authenticate(request: HttpServletRequest): ResponseEntity<UserDto> {
        val session = request.getSession(false) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        // SPRING_SECURITY_CONTEXT라는 attribute 이름으로 세션에 인증 토큰이 저장된다.
        val securityContextImpl = session.getAttribute("SPRING_SECURITY_CONTEXT") as SecurityContextImpl
        val userDto = securityContextImpl.authentication.principal as UserDto
        return ResponseEntity.ok(userDto)
    }
}