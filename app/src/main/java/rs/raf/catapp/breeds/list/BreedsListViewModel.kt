package rs.raf.catapp.breeds.list

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.breeds.api.model.BreedApiModel
import rs.raf.catapp.breeds.api.model.asBreedUiModel
import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.breeds.repository.BreedRepository
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

class BreedsListViewModel(
    private val repository: BreedRepository = BreedRepository
): ViewModel() {

    private val _state = MutableStateFlow(BreedListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedListState.() -> BreedListState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<BreedsListUiEvent>()
    fun setEvent(event: BreedsListUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchBreeds()
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            events
                .filterIsInstance<BreedsListUiEvent.SearchQueryChanged>()
                .debounce(1.5.seconds)
                .collect {
                    setState { copy(filteredBreeds = breeds.filter { breed ->
                            if (query == "")
                                breeds
                            breed.name.lowercase().contains(query.lowercase())
                        })
                    }
                }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect{
                when (it) {
                    BreedsListUiEvent.ClearSearch -> Unit
                    BreedsListUiEvent.CloseSearchMode -> setState { copy(isSearchMode = false) }
                    is BreedsListUiEvent.SearchQueryChanged -> {
                        searchQuery(it.query)
                    }
                }
            }
        }
    }

    private fun searchQuery(query: String) {
        viewModelScope.launch {
            setState { copy(query = query) }
        }
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val breeds = withContext(Dispatchers.IO) {
                    repository.fetchAllBreeds().map { it.asBreedUiModel() }
                }
                setState { copy(breeds = breeds) }
                setState { copy(filteredBreeds = breeds) }
            } catch (error: IOException) {
                setState { copy(error = BreedListState.ListError.LoadingFailed(cause = error)) }
            } finally {
                setState { copy(loading = false) }
            }
        }
    }
}