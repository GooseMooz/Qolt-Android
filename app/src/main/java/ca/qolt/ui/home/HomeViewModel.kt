package ca.qolt.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qolt.data.repository.UsageSessionRepository
import ca.qolt.domain.SessionTrackingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usageSessionRepository: UsageSessionRepository,
    private val sessionTrackingManager: SessionTrackingManager
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    init {
        refreshStreak()
    }

    /**
     * Refresh the current streak value from the database.
     */
    fun refreshStreak() {
        viewModelScope.launch {
            try {
                val streak = usageSessionRepository.calculateStreak()
                _currentStreak.value = streak
                Timber.tag(TAG).d("Refreshed streak: $streak days")
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Error refreshing streak")
                _currentStreak.value = 0
            }
        }
    }

    /**
     * End the current session (called when user unblocks via NFC or emergency).
     * Also refreshes the streak after ending the session.
     */
    suspend fun endSession() {
        try {
            sessionTrackingManager.endCurrentSession()
            refreshStreak()
            Timber.tag(TAG).d("Session ended and streak refreshed")
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Error ending session")
        }
    }
}
