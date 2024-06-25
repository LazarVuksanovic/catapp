package rs.raf.catapp.breeds.list

import rs.raf.catapp.breeds.list.model.BreedUiModel
import rs.raf.catapp.users.model.UserUiModel

data class BreedListState(
    val loading: Boolean = false,
    val breeds: List<BreedUiModel> = emptyList(),
    val error: ListError? = null,
    val query: String = "",
    val isSearchMode: Boolean = false,
    val filteredBreeds: List<BreedUiModel> = emptyList(),
    val user: UserUiModel? = null
){
    sealed class ListError {
        data class LoadingFailed(val cause: Throwable? = null) : ListError()
    }
}