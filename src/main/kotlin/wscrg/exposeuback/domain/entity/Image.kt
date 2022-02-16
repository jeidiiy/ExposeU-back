package wscrg.exposeuback.domain.entity

import javax.persistence.*

@Entity
class Image(
    photo: ByteArray?
): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Lob
    var photo = photo
        protected set

    fun changePhoto(photo: ByteArray?) {
        this.photo = photo
    }
}