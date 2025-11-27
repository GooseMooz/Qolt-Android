package ca.qolt.domain

import ca.qolt.data.local.SessionManager
import ca.qolt.data.repository.UsageSessionRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * High-level manager for session tracking operations.
 * Provides a simple interface for UI components to end sessions.
 */
@Singleton
class SessionTrackingManager @Inject constructor(
    private val usageSessionRepository: UsageSessionRepository,
    private val sessionManager: SessionManager
) {
    companion object {
        private const val TAG = "SessionTrackingMgr"
    }

    /**
     * End the current active session and clear session state.
     * This should be called when the user explicitly stops blocking (NFC or emergency unblock).
     */
    suspend fun endCurrentSession() {
        val sessionId = sessionManager.getCurrentSessionId()
        if (sessionId != null) {
            usageSessionRepository.endSession(sessionId)
            sessionManager.clearCurrentSessionId()
            Timber.tag(TAG).d("Ended current session: $sessionId")
        } else {
            Timber.tag(TAG).w("No active session to end")
        }
    }
}
