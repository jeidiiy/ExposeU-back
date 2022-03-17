package wscrg.exposeuback.domain.entity

import javax.persistence.*

@Entity
@Table(name = "likes")
class Like(
    user: User,
    image: UploadFile
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    var user = user
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPLOAD_FILE_ID", nullable = false)
    var image = image
        protected set

    override fun toString(): String {
        return "Like(id=$id, user=$user, image=$image)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Like

        if (id != other.id) return false
        if (user != other.user) return false
        if (image != other.image) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + user.hashCode()
        result = 31 * result + image.hashCode()
        return result
    }
}
