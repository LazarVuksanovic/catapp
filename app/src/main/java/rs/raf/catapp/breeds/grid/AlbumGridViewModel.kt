package rs.edu.raf.rma.photos.albums.grid

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.breeds.grid.BreedGridState
import rs.raf.catapp.breeds.list.BreedListState
import rs.raf.catapp.images.repository.ImageRepository
import rs.raf.catapp.images.repository.asImageUiModel
import rs.raf.catapp.navigation.dataId
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AlbumGridViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val breedId: String = savedStateHandle.dataId

    private val _state = MutableStateFlow(BreedGridState(breedId = breedId))
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedGridState.() -> BreedGridState) = _state.update(reducer)

    init {
        fetchAlbums()
        observeImages()


        // TODO We want to show album owner name
        //observeUser()
    }

    private fun fetchAlbums() {
        viewModelScope.launch {
            setState { copy(updating = true) }
            try {
                withContext(Dispatchers.IO) {
                    imageRepository.getBreedImages(breedId)
                }
            } catch (error: Exception) {
                Log.d("RMA", "Exception", error)
            }
            setState { copy(updating = false) }
        }
    }

    private fun observeImages() {
        viewModelScope.launch {
            imageRepository.observeBreedImages(breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(images = it.map { it.asImageUiModel() }) }
                }
        }
    }
}
