package rs.raf.catapp.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapp.breeds.list.BreedListState
import rs.raf.catapp.leaderboard.api.LeaderboardApi
import rs.raf.catapp.leaderboard.model.asUiModel
import rs.raf.catapp.leaderboard.repository.LeaderboardRepository
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
): ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()

    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) = _state.getAndUpdate(reducer)

    init {
        fetchLeaderboard()
    }

    private fun fetchLeaderboard() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                var data = withContext(Dispatchers.IO){
                    repository.getLeaderboard()
                }
                data = data.sortedByDescending { it.result }
                setState { copy(leaderboard = data.map { it.asUiModel() }) }
            } catch (error: IOException) {
                setState { copy(error = Exception() )}
            } finally {
                setState { copy(loading = false) }
            }
        }
    }
}