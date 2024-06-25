package rs.raf.catapp.breeds.repository

import androidx.room.withTransaction
import rs.raf.catapp.breeds.api.BreedsApi
import rs.raf.catapp.breeds.api.model.BreedApiModel
import rs.raf.catapp.breeds.db.Breed
import rs.raf.catapp.breeds.db.BreedWithImage
import rs.raf.catapp.breeds.mappers.asBreedDbModel
import rs.raf.catapp.db.AppDatabase
import rs.raf.catapp.images.db.Image
import javax.inject.Inject


class BreedRepository @Inject constructor(
    private val breedsApi: BreedsApi,
    private val database: AppDatabase,
) {

    suspend fun getAllBreeds(): List<Breed> {
        return database.breedDao().getAll()
    }

    suspend fun getAllWithImage(): List<BreedWithImage> {
        return database.breedDao().getAllWithImages()
    }

    fun observeBreed(id: String) = database.breedDao().observeBreed(id)

    fun observeAllBreeds() = database.breedDao().observeAll()

    suspend fun fetchBreedDetails(breedId: String) : BreedWithImage {
        return database.breedDao().getBreed(breedId)
    }

    suspend fun fetchAllBreeds() {
        val allBreeds = breedsApi.getAllBreeds()

        val allPhotos = mutableListOf<Image>()
        allBreeds.forEachIndexed { index, breed ->
            breed.image?.id?.let {
                Image(
                    id = it, height = breed.image.height,
                    width = breed.image.width, url = breed.image.url, breedId = breed.id
                )
            }?.let { allPhotos.add(it) }
        }

        database.withTransaction {
            database.breedDao().upsertAllBreeds(allBreeds
                .map { it.asBreedDbModel() }
                .toMutableList())
            database.imageDao().upsertAllImages(allPhotos)
        }

    }
}