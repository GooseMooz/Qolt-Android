package ca.qolt.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENT_SESSION_ID = "current_session_id"
        private const val KEY_LAST_SERVICE_CHECK = "last_service_check"
    }

    /**
     * Save the ID of the currently active session.
     */
    fun saveCurrentSessionId(sessionId: Long) {
        prefs.edit().putLong(KEY_CURRENT_SESSION_ID, sessionId).apply()
    }

    /**
     * Get the ID of the currently active session.
     * @return Session ID or null if no active session
     */
    fun getCurrentSessionId(): Long? {
        val id = prefs.getLong(KEY_CURRENT_SESSION_ID, -1L)
        return if (id == -1L) null else id
    }

    /**
     * Clear the current session ID.
     */
    fun clearCurrentSessionId() {
        prefs.edit().remove(KEY_CURRENT_SESSION_ID).apply()
    }

    /**
     * Save the timestamp of the last service health check.
     * This is used to estimate session duration if service is killed.
     */
    fun saveLastServiceCheckTime(timestampMs: Long) {
        prefs.edit().putLong(KEY_LAST_SERVICE_CHECK, timestampMs).apply()
    }

    /**
     * Get the timestamp of the last service health check.
     * @return Timestamp or null if never checked
     */
    fun getLastServiceCheckTime(): Long? {
        val time = prefs.getLong(KEY_LAST_SERVICE_CHECK, -1L)
        return if (time == -1L) null else time
    }
}
