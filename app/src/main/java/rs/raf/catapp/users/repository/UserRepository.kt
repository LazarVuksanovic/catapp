package rs.raf.catapp.users.repository

import rs.raf.catapp.db.AppDatabase
import rs.raf.catapp.users.db.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val database: AppDatabase
) {

    suspend fun fetchUsers() : User = database.userDao().getUser()

    fun observeUser() = database.userDao().observeUser()
    fun createUser(firstName: String, lastName: String, mail: String, username: String) {
        database.userDao().insert(
            User(firstName = firstName,
                lastName = lastName,
                mail = mail,
                username = username)
        )
    }
    fun getUser() = database.userDao().getUser()
    fun updateUser(user: User) = database.userDao().updateUser(user)


}