package ca.qolt.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ca.qolt.data.local.entity.UsageSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageSessionDao {

    @Query("SELECT * FROM usage_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<UsageSessionEntity>>

    @Query("SELECT * FROM usage_sessions WHERE startTime >= :startTime AND startTime <= :endTime ORDER BY startTime DESC")
    fun getSessionsInRange(startTime: Long, endTime: Long): Flow<List<UsageSessionEntity>>

    @Query("SELECT * FROM usage_sessions WHERE endTime IS NULL LIMIT 1")
    suspend fun getActiveSession(): UsageSessionEntity?

    @Query("SELECT * FROM usage_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getSessionById(sessionId: Long): UsageSessionEntity?

    @Insert
    suspend fun insertSession(session: UsageSessionEntity): Long

    @Update
    suspend fun updateSession(session: UsageSessionEntity)

    @Query("SELECT SUM(durationMs) FROM usage_sessions WHERE startTime >= :startTime")
    suspend fun getTotalDurationSince(startTime: Long): Long?

    @Query("""
        SELECT SUM(durationMs) FROM usage_sessions
        WHERE startTime >= :dayStart
        AND startTime < :dayEnd
        AND endTime IS NOT NULL
    """)
    suspend fun getTotalDurationForDay(dayStart: Long, dayEnd: Long): Long?

    @Query("DELETE FROM usage_sessions")
    suspend fun deleteAllSessions()
}
