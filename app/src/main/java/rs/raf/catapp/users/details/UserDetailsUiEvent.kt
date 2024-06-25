package rs.raf.catapp.users.details

sealed class UserDetailsUiEvent {
    data class UpdateUser(
        val firstName: String, val lastName: String, val username: String,  val mail: String,
        val id: Int?, val ranking: Int?,
    ) : UserDetailsUiEvent()
}