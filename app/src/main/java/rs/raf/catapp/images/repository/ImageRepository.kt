package rs.raf.catapp.images.repository

import rs.raf.catapp.breeds.gallery.model.ImageUiModel
import rs.raf.catapp.db.AppDatabase
import rs.raf.catapp.images.api.ImageApi
import rs.raf.catapp.images.api.model.ImageApiModel
import rs.raf.catapp.images.db.Image
import javax.inject.Inject


class ImageRepository @Inject constructor(
    private val imageApi: ImageApi,
    private val database: AppDatabase,
) {
    suspend fun getCoverImageForBreed(id: String): Image {
        val image = imageApi.getAllBreedImages(id)
        database.imageDao().insertAll(list = image.map {it.asImageDbModel()})
        return database.imageDao().getCoverImageOfBreed(id)
    }

    suspend fun getBreedImages(id: String): List<Image>{
        var images = database.imageDao().getImagesOfBreed(id)
        if(images.isEmpty()){
            images = imageApi.getAllBreedImages(breedId = id).map { it.asImageDbModel() }
            database.imageDao().upsertAllImages(images)
            return database.imageDao().getImagesOfBreed(id)
        }
        return images
    }

    fun observeBreedImages(id: String) = database.imageDao().observeImagesOfBreed(id)
}

fun Image.asImageUiModel() = ImageUiModel(
    id = this.id,
    url = this.url,
    height = this.height,
    width = this.width,
    breedId = this.breedId
)

fun ImageApiModel.asImageDbModel() = Image(
    id = this.id,
    url = this.url,
    height = this.height,
    width = this.width,
    breedId = this.breeds[0].id,
)
