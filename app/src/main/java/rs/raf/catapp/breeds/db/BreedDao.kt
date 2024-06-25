package rs.raf.catapp.breeds.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(breedData: Breed)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Breed>)

    @Query("SELECT * FROM Breed")
    suspend fun getAll(): List<Breed>

    @Query("SELECT * FROM Breed")
    suspend fun getAllWithImages(): List<BreedWithImage>

    @Query("SELECT * FROM Breed")
    fun observeAll(): Flow<List<BreedWithImage>>

    @Query("SELECT * FROM Breed WHERE id = :id")
    fun observeBreed(id: String): Flow<BreedWithImage>

    @Query("SELECT * FROM Breed WHERE id = :id")
    suspend fun getBreed(id: String): BreedWithImage

    @Upsert
    fun upsertAllBreeds(list: List<Breed>)
}