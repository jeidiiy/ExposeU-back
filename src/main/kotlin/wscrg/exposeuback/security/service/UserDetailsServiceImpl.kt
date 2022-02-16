package wscrg.exposeuback.security.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import wscrg.exposeuback.repository.UserRepository

@Service("userDetailsService")
class UserDetailsServiceImpl(
    val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException(String.format("No User founded with username: %s", email))

        //TODO: 권한 추가
        return with(user) { User(email, password, setOf(SimpleGrantedAuthority("ROLE_USER"))) }
    }
}