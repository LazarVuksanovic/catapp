package rs.raf.catapp.images.api.model

import kotlinx.serialization.Serializable
import rs.raf.catapp.breeds.api.model.BreedApiModel

@Serializable
data class ImageApiModel(
    val breeds: List<BreedApiModel>,
    val id: String,
    val url: String,
    val height: Int,
    val width: Int,
)

