package wscrg.exposeuback.domain.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class UploadFile(
    uploadFileName: String?,
    storeFileName: String?,
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    var uploadFileName: String? = null
        protected set

    var storeFileName: String? = null
        protected set

    override fun toString(): String {
        return "UploadFile(id=$id, uploadFileName=$uploadFileName, storeFileName=$storeFileName)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UploadFile

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
