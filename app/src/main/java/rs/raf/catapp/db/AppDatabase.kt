package rs.raf.catapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import rs.raf.catapp.breeds.db.Breed
import rs.raf.catapp.breeds.db.BreedDao
import rs.raf.catapp.images.db.Image
import rs.raf.catapp.images.db.ImageDao
import rs.raf.catapp.quiz.db.PlayedQuiz
import rs.raf.catapp.quiz.db.PlayedQuizDao
import rs.raf.catapp.users.db.User
import rs.raf.catapp.users.db.UserDao


@Database(
    entities = [
        Breed::class,
        Image::class,
        User::class,
        PlayedQuiz::class
    ],
    version = 9,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breedDao(): BreedDao
    abstract fun imageDao(): ImageDao
    abstract fun userDao(): UserDao
    abstract fun playedQuizDao(): PlayedQuizDao

}
