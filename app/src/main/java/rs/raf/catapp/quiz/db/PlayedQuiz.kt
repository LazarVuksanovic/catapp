package rs.raf.catapp.quiz.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayedQuiz (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userId: Int,
    val score: Double,
    val rightAnswers: Int
)