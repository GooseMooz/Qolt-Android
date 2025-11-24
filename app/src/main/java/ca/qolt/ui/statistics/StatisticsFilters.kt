package ca.qolt.ui.statistics

import java.time.LocalDate

/**
 * Represents the time period view (Weekly or Monthly)
 */
enum class TimePeriod {
    WEEKLY,
    MONTHLY
}

/**
 * Represents the duration filter (1 Day, 7 Days, 30 Days)
 */
enum class Duration(val days: Int) {
    ONE_DAY(1),
    SEVEN_DAYS(7),
    THIRTY_DAYS(30)
}

/**
 * Represents the current filter state for the statistics screen
 */
data class StatisticsFilters(
    val timePeriod: TimePeriod = TimePeriod.WEEKLY,
    val duration: Duration = Duration.SEVEN_DAYS,
    val selectedDate: LocalDate = LocalDate.now(),
    val searchQuery: String = ""
) {
    /**
     * Returns the start date based on duration and selected date
     */
    fun getStartDate(): LocalDate {
        return when (duration) {
            Duration.ONE_DAY -> selectedDate
            Duration.SEVEN_DAYS -> selectedDate.minusDays(6)
            Duration.THIRTY_DAYS -> selectedDate.minusDays(29)
        }
    }
    
    /**
     * Returns the end date (always the selected date)
     */
    fun getEndDate(): LocalDate = selectedDate
    
    /**
     * Returns a human-readable description of the filter
     */
    fun getDescription(): String {
        return when (duration) {
            Duration.ONE_DAY -> "Today"
            Duration.SEVEN_DAYS -> "Last 7 days"
            Duration.THIRTY_DAYS -> "Last 30 days"
        }
    }
}

