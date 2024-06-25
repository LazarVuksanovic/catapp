package rs.raf.catapp.breeds.gallery
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.breeds.gallery.model.ImageUiModel
import rs.raf.catapp.images.db.Image
import rs.raf.catapp.images.repository.ImageRepository
import rs.raf.catapp.images.repository.asImageUiModel
import rs.raf.catapp.navigation.dataId
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val breedId: String = savedStateHandle.dataId

    private val _state = MutableStateFlow(BreedGalleryState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedGalleryState.() -> BreedGalleryState) = _state.getAndUpdate(reducer)

    init {
        fetchImages()
        observeImages()
    }

    private fun observeImages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                imageRepository.observeBreedImages(breedId).collect {
                    setState { copy(images = it.map {it.asImageUiModel()}) }
                }
            }

        }
    }

    private fun fetchImages() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO){
                    imageRepository.getBreedImages(breedId)
                }
            } catch (error: IOException) {
                setState { copy(error = error) }
            } finally {
                setState { copy(loading = false) }
            }
        }
    }
}
