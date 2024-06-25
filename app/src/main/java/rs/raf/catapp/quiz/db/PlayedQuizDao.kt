package rs.raf.catapp.quiz.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayedQuizDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(playedQuiz: PlayedQuiz)


    @Query("SELECT * FROM PlayedQuiz")
    fun observeGames() : Flow<List<PlayedQuiz>>
}