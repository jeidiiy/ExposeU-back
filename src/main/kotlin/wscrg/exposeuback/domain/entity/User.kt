package wscrg.exposeuback.domain.entity

import wscrg.exposeuback.domain.dto.user.UserSignUpRequestDto
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    email: String,
    name: String,
    password: String,
    phoneNumber: String?,
    image: Image?,
    portfolio: Portfolio?
) : BaseTimeEntity() {

    companion object {
        fun of(dto: UserSignUpRequestDto) = with(dto) {
            UserBuilder(email = email, password = password, name = name).image(image).build()
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
    var name = name
        protected set

    @Column(nullable = false)
    var password = password
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_ID", nullable = true)
    var image = image
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PORTFOLIO_ID", nullable = true)
    var portfolio = portfolio
        protected set

    @Column(nullable = true)
    var phoneNumber = phoneNumber
        protected set

    class UserBuilder(
        private var email: String,
        private var password: String,
        private var name: String
    ) {
        private var phoneNumber: String? = null
        private var image: Image? = null
        private var portfolio: Portfolio? = null

        fun email(email: String) = apply {
            this.email = email
        }

        fun password(password: String) = apply {
            this.password = password
        }

        fun name(name: String) = apply {
            this.name = name
        }

        fun phoneNumber(phoneNumber: String?) = apply {
            this.phoneNumber = phoneNumber
        }

        fun image(image: Image?) = apply {
            this.image = image
        }

        fun portfolio(portfolio: Portfolio?) = apply {
            this.portfolio = portfolio
        }

        fun build() = User(email, password, name, phoneNumber, image, portfolio)
    }
}