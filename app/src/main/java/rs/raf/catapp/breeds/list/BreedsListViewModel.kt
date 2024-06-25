package rs.raf.catapp.breeds.list

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.breeds.api.model.BreedApiModel
import rs.raf.catapp.breeds.db.Breed
import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.breeds.repository.BreedRepository
import rs.raf.catapp.images.repository.ImageRepository
import rs.raf.catapp.users.db.asUserUIModel
import rs.raf.catapp.users.repository.UserRepository
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class BreedsListViewModel @Inject constructor(
    private val repository: BreedRepository,
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(BreedListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedListState.() -> BreedListState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<BreedsListUiEvent>()
    fun setEvent(event: BreedsListUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        observeBreeds()
        fetchBreeds()
        observeSearchQuery()
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            userRepository.observeUser()
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(user = it.asUserUIModel())
                    }
                }
        }
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            repository.observeAllBreeds()
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(
                            loading = false,
                            breeds = it.map { it.asBreedUiModel() },
                            filteredBreeds = it.map { it.asBreedUiModel() },
                        )
                    }
                }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            events
                .filterIsInstance<BreedsListUiEvent.SearchQueryChanged>()
                .debounce(1.5.seconds)
                .collect {
                    applySearchQuery()
                }
        }
    }

    private fun applySearchQuery() {
        viewModelScope.launch {
            setState {
                copy(filteredBreeds = breeds.filter { breed ->
                    if (query == "")
                        breeds
                    breed.name.lowercase().contains(query.lowercase())
                })
            }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect{
                when (it) {
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
                withContext(Dispatchers.IO){
                    repository.fetchAllBreeds()
                }
            } catch (error: IOException) {
                setState { copy(error = BreedListState.ListError.LoadingFailed(cause = error)) }
            } finally {
                setState { copy(loading = false) }
            }
        }
    }
}