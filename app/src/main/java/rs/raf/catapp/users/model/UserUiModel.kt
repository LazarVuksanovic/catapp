package rs.raf.catapp.users.model

data class UserUiModel(
    val id: Int?,
    val firstName: String,
    val lastName: String,
    val username: String,
    val mail: String,
    val ranking: Int
)
