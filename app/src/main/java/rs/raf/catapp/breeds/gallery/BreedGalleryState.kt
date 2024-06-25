package rs.raf.catapp.breeds.gallery

import rs.raf.catapp.breeds.gallery.model.ImageUiModel

data class BreedGalleryState(
        val images: List<ImageUiModel> = emptyList(),
        val loading: Boolean = false,
        val error: Exception? = null
    )