package ca.qolt.data.repository

import android.content.Context
import ca.qolt.AppBlockingManager
import ca.qolt.data.local.SessionManager
import ca.qolt.data.local.dao.UsageSessionDao
import ca.qolt.data.local.entity.UsageSessionEntity
import ca.qolt.util.TimeUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface UsageSessionRepository {
    suspend fun startSession(blockedAppsCount: Int): Long
    suspend fun endSession(sessionId: Long)
    suspend fun getActiveSession(): UsageSessionEntity?
    suspend fun getTotalDurationForDay(dayStartMs: Long, dayEndMs: Long): Long
    suspend fun calculateStreak(thresholdMs: Long = 7_200_000L): Int
    suspend fun closeAnyOrphanedSessions()
}

@Singleton
class UsageSessionRepositoryImpl @Inject constructor(
    private val usageSessionDao: UsageSessionDao,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
) : UsageSessionRepository {

    companion object {
        private const val TAG = "UsageSessionRepo"
        private const val TWO_HOURS_MS = 7_200_000L // 2 hours in milliseconds
    }

    /**
     * Start a new blocking session.
     * @return The ID of the created session
     */
    override suspend fun startSession(blockedAppsCount: Int): Long {
        val session = UsageSessionEntity(
            startTime = System.currentTimeMillis(),
            endTime = null,
            durationMs = 0L,
            blockedAppsCount = blockedAppsCount
        )
        val sessionId = usageSessionDao.insertSession(session)
        Timber.tag(TAG).d("Started session $sessionId with $blockedAppsCount apps")
        return sessionId
    }

    /**
     * End an active session.
     */
    override suspend fun endSession(sessionId: Long) {
        val session = usageSessionDao.getSessionById(sessionId) ?: run {
            Timber.tag(TAG).w("Cannot end session $sessionId - not found")
            return
        }

        val endTime = System.currentTimeMillis()
        val duration = endTime - session.startTime

        val updatedSession = session.copy(
            endTime = endTime,
            durationMs = duration
        )

        usageSessionDao.updateSession(updatedSession)
        Timber.tag(TAG).d("Ended session $sessionId - duration: ${duration / 1000}s")
    }

    /**
     * Get the currently active session (one without end time).
     */
    override suspend fun getActiveSession(): UsageSessionEntity? {
        return usageSessionDao.getActiveSession()
    }

    /**
     * Get total blocking duration for a specific day.
     * @param dayStartMs Start of day timestamp (00:00:00.000)
     * @param dayEndMs End of day timestamp (23:59:59.999)
     * @return Total duration in milliseconds
     */
    override suspend fun getTotalDurationForDay(dayStartMs: Long, dayEndMs: Long): Long {
        return usageSessionDao.getTotalDurationForDay(dayStartMs, dayEndMs) ?: 0L
    }

    /**
     * Calculate the current streak - consecutive days with >= threshold duration.
     * Uses simple approach: session credited entirely to start day.
     *
     * @param thresholdMs Minimum duration per day (default: 2 hours)
     * @return Number of consecutive days meeting threshold
     */
    override suspend fun calculateStreak(thresholdMs: Long): Int {
        var currentStreak = 0
        var checkDate = System.currentTimeMillis() // Start from today

        // Check backwards from today
        while (currentStreak < 365) { // Safety limit to avoid infinite loop
            val dayStart = TimeUtils.getStartOfDayMs(checkDate)
            val dayEnd = TimeUtils.getEndOfDayMs(checkDate)

            val dayDuration = getTotalDurationForDay(dayStart, dayEnd)

            Timber.tag(TAG).v("Day ${dayStart}: duration=${dayDuration}ms, threshold=${thresholdMs}ms")

            if (dayDuration >= thresholdMs) {
                currentStreak++
                // Go back one day (24 hours in milliseconds)
                checkDate = dayStart - 86_400_000L
            } else {
                // Streak broken
                break
            }
        }

        Timber.tag(TAG).d("Calculated streak: $currentStreak days")
        return currentStreak
    }

    /**
     * Close any orphaned sessions from previous crashes.
     * A session is orphaned if it has no end time but blocking is not active.
     */
    override suspend fun closeAnyOrphanedSessions() {
        val activeSession = usageSessionDao.getActiveSession()

        if (activeSession != null) {
            val isStillBlocking = AppBlockingManager.isBlockingActive(context)

            if (!isStillBlocking) {
                // Session should have ended but didn't (probably crash/force stop)
                Timber.tag(TAG).w("Found orphaned session ${activeSession.id} - closing it")

                // Use last check time if available, otherwise use current time
                val lastCheckTime = sessionManager.getLastServiceCheckTime()
                    ?: System.currentTimeMillis()

                val endTime = minOf(lastCheckTime, System.currentTimeMillis())
                val duration = endTime - activeSession.startTime

                val updatedSession = activeSession.copy(
                    endTime = endTime,
                    durationMs = duration
                )

                usageSessionDao.updateSession(updatedSession)
                Timber.tag(TAG).i("Closed orphaned session ${activeSession.id}")
            } else {
                Timber.tag(TAG).d("Active session ${activeSession.id} is still valid")
            }
        }
    }
}
