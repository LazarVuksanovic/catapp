package rs.raf.catapp.users.login

sealed class UserLoginUiEvent {
    data class CreateUser(
        val firstName: String, val lastName: String, val username: String,  val mail: String
        ) : UserLoginUiEvent()
//    data class UpdateUser(
//        val firstName: String, val lastName: String, val username: String,  val mail: String,
//        val id: Int?, val ranking: Int?,
//    ) : UserLoginUiEvent()
}