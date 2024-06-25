package rs.raf.catapp.images.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image (
    @PrimaryKey
    val id: String,
    val url: String?,
    val width: Int?,
    val height: Int?,
    val breedId: String,
)
