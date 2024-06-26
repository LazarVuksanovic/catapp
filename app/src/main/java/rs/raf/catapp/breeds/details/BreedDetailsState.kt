package rs.raf.catapp.breeds.details

import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.images.db.Image

data class BreedDetailsState(
    val breedId: String,
    val loading: Boolean = false,
    val data: BreedUiModel? = null,
    val error: DetailsError? = null,
){
    sealed class DetailsError {
        data class LoadingFailed(val cause: Throwable? = null) : DetailsError()
    }
}
