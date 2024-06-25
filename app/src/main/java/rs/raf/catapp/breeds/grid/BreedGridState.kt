package rs.raf.catapp.breeds.grid

import rs.raf.catapp.breeds.gallery.model.ImageUiModel

data class BreedGridState(
    val updating: Boolean = false,
    val images: List<ImageUiModel> = emptyList(),
    val breedId: String,
)
