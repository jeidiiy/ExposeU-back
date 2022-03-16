package wscrg.exposeuback.api.user.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wscrg.exposeuback.domain.dto.authentication.UserDto
import wscrg.exposeuback.service.LikeService

@RequestMapping("/api/like")
@RestController
class LikeApiController(
    val likeService: LikeService
) {
    @PostMapping("/{imageId}")
    fun addLike(
        @AuthenticationPrincipal user: UserDto?,
        @PathVariable imageId: Long?,
    ): ResponseEntity<String> {
        if (user != null) {
            likeService.addLike(user, imageId ?: -1L)
            return ResponseEntity(HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }
}
