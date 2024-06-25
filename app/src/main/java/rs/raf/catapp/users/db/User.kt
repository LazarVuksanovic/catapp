package rs.raf.catapp.users.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import rs.raf.catapp.users.model.UserUiModel

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val username: String,
    val mail: String,
    var ranking: Int? = -1
)

fun User.asUserUIModel() = this.ranking?.let {
    UserUiModel(
        id = this.id,
        lastName = this.lastName,
        firstName = this.firstName,
        username = this.username,
        mail = this.mail,
        ranking = it
    )
}
