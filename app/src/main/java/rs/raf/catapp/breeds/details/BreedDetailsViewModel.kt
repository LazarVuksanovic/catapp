package rs.raf.catapp.breeds.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.breeds.repository.BreedRepository
import rs.raf.catapp.images.repository.ImageRepository
import rs.raf.catapp.images.repository.asImageUiModel
import rs.raf.catapp.navigation.dataId
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val breedId: String = savedStateHandle.dataId

    private val _state = MutableStateFlow(BreedDetailsState(breedId = breedId))
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedDetailsState.() -> BreedDetailsState) = _state.getAndUpdate(reducer)

    init {
        fetchBreedDetails()
        observeBreed()
    }

    private fun observeBreed() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.observeBreed(breedId).distinctUntilChanged().collect {
                    setState { copy(data = it.asBreedUiModel())}
                }
            }
        }
    }

    private fun fetchBreedDetails(){
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO){
                    repository.fetchBreedDetails(breedId = breedId)
                }
            } catch (error: IOException){
                setState { copy(error = BreedDetailsState.DetailsError.LoadingFailed(cause = error)) }
            }   finally {
                setState { copy(loading = false) }
            }
        }
    }
//    private fun fetchBreedImages(){
//        viewModelScope.launch {
//            try{
//                val data = withContext(Dispatchers.IO) {
//                    withContext(Dispatchers.IO) {
//                        imageRepository.getBreedImages(breedId)
//                    }
//                }
//                setState { copy(images = data) }
//            } catch (error: IOException){
//                setState { copy(error = BreedDetailsState.DetailsError.LoadingFailed(cause = error)) }
//            }   finally {
//            }
//        }
//    }
}