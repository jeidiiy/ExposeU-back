package wscrg.exposeuback.domain.entity

import javax.persistence.*

@Entity
@Table(name = "likes")
class Like(
    user: User,
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    var user = user
        protected set
}