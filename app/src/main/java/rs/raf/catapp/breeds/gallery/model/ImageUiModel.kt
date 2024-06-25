package rs.raf.catapp.breeds.gallery.model

data class ImageUiModel (
    val id: String,
    val url: String?,
    val width: Int?,
    val height: Int?,
    val breedId: String,
)