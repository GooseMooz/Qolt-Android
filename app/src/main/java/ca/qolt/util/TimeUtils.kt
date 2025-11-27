package ca.qolt.util

import java.util.Calendar

object TimeUtils {

    /**
     * Get the start of the day (midnight) for a given timestamp in local timezone.
     * @param timestampMs The timestamp in milliseconds (defaults to current time)
     * @return The timestamp at 00:00:00.000 of the day
     */
    fun getStartOfDayMs(timestampMs: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestampMs
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * Get the end of the day (23:59:59.999) for a given timestamp in local timezone.
     * @param timestampMs The timestamp in milliseconds (defaults to current time)
     * @return The timestamp at 23:59:59.999 of the day
     */
    fun getEndOfDayMs(timestampMs: Long = System.currentTimeMillis()): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestampMs
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    /**
     * Get the start of day timestamp for N days ago.
     * @param daysAgo Number of days to go back (0 = today)
     * @return The timestamp at 00:00:00.000 of that day
     */
    fun getDaysAgoStartMs(daysAgo: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * Check if two timestamps are on the same day in local timezone.
     * @param timestamp1Ms First timestamp
     * @param timestamp2Ms Second timestamp
     * @return true if both timestamps are on the same day
     */
    fun isSameDayLocal(timestamp1Ms: Long, timestamp2Ms: Long): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.timeInMillis = timestamp1Ms
        cal2.timeInMillis = timestamp2Ms

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}
