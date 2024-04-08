package rs.raf.catapp.breeds.list

sealed class BreedsListUiEvent {
    data class SearchQueryChanged(val query: String) : BreedsListUiEvent()
}