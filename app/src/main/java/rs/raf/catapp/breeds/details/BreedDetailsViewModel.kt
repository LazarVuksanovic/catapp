package rs.raf.catapp.breeds.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.breeds.api.model.asBreedUiModel
import rs.raf.catapp.breeds.repository.BreedRepository
import java.io.IOException

class BreedDetailsViewModel(
    private val breedId: String,
    private val repository: BreedRepository = BreedRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BreedDetailsState(breedId = breedId))
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedDetailsState.() -> BreedDetailsState) = _state.getAndUpdate(reducer)

    init {
        fetchBreedDetails()
    }

    private fun fetchBreedDetails(){
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val data = withContext(Dispatchers.IO){
                    repository.fetchBreedDetails(breedId = breedId)?.asBreedUiModel()
                }
                setState { copy(data = data) }
            } catch (error: IOException){
                setState { copy(error = BreedDetailsState.DetailsError.LoadingFailed(cause = error)) }
            }   finally {
                setState { copy(loading = false) }
            }
        }
    }
}