package wscrg.exposeuback.domain.entity

import wscrg.exposeuback.domain.dto.user.UserSignUpRequestDto
import javax.persistence.*

@Entity
@Table(name = "users")
class User private constructor(
    email: String,
    username: String,
    password: String,
    phoneNumber: String?,
    image: UploadFile?
) : BaseTimeEntity() {
    companion object {
        fun of(dto: UserSignUpRequestDto) = with(dto) {
            UserBuilder(email = email, password = password, username = username).image(image).build()
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false, unique = true)
    var email = email
        protected set

    @Column(nullable = false)
    var username = username
        protected set

    @Column(nullable = false)
    var password = password
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPLOAD_FILE_ID", nullable = false)
    var image = image
        protected set

    @Column(nullable = true)
    var phoneNumber = phoneNumber
        protected set

    class UserBuilder(
        private var email: String,
        private var username: String,
        private var password: String
    ) {
        private var phoneNumber: String? = null
        private var image: UploadFile? = null

        fun email(email: String) = apply {
            this.email = email
        }

        fun password(password: String) = apply {
            this.password = password
        }

        fun username(username: String) = apply {
            this.username = username
        }

        fun phoneNumber(phoneNumber: String?) = apply {
            this.phoneNumber = phoneNumber
        }

        fun image(image: UploadFile?) = apply {
            this.image = image
        }

        fun build() =
            User(
                email = email,
                username = username,
                password = password,
                phoneNumber = phoneNumber,
                image = image
            )
    }
}
