package rs.raf.catapp.images.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Image>)

    @Query("SELECT * FROM Image WHERE id = :imageId")
    fun getCoverImageOfBreed(imageId: String): Image

    @Transaction
    @Query("SELECT * FROM Image WHERE breedid = :breedId")
    suspend fun getImagesOfBreed(breedId: String): List<Image>

    @Query("SELECT * FROM Image WHERE breedid = :breedId")
    fun observeImagesOfBreed(breedId: String): Flow<List<Image>>

    @Upsert
    fun upsertAllImages(list: List<Image>)
}