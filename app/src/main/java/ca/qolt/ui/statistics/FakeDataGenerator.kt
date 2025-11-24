package ca.qolt.ui.statistics

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * Generates fake data for widgets. This is a demo-ready implementation
 * that can be easily replaced with real data sources later.
 */
object FakeDataGenerator {
    
    private val random = Random(42) // Fixed seed for consistent demo data
    
    fun generateFocusTimeWidget(): WidgetType.FocusTime {
        return WidgetType.FocusTime(
            totalHours = 38.4f,
            period = "this week",
            todayHours = 6.5f,
            trendPercentage = 21f
        )
    }
    
    fun generateStreakWidget(): WidgetType.Streak {
        return WidgetType.Streak(
            currentStreak = 12,
            targetStreak = 30
        )
    }
    
    fun generateWeeklyGoalWidget(): WidgetType.WeeklyGoal {
        return WidgetType.WeeklyGoal(
            currentProgress = 5,
            targetProgress = 7,
            percentage = 85
        )
    }
    
    fun generateAppUsageWidget(): WidgetType.AppUsage {
        val apps = listOf(
            AppUsageItem("Instagram", 2.5f, 0xFFE4405F, 35f),
            AppUsageItem("Email", 1.8f, 0xFF2196F3, 25f),
            AppUsageItem("Messages", 1.2f, 0xFF4CAF50, 17f),
            AppUsageItem("YouTube", 3.1f, 0xFFFF0000, 43f)
        )
        return WidgetType.AppUsage(apps = apps)
    }
    
    fun generateTotalHoursWidget(): WidgetType.TotalHours {
        return WidgetType.TotalHours(
            hours = 2228.28f,
            period = "all time"
        )
    }
    
    fun generateCircularProgressWidget(): WidgetType.CircularProgress {
        return WidgetType.CircularProgress(
            current = 28f,
            total = 228f,
            percentage = 12
        )
    }
    
    fun generateBarChartWidget(): WidgetType.BarChart {
        val dataPoints = listOf(28f, 32f, 35f, 37f)
        val labels = listOf("W1", "W2", "W3", "W4")
        return WidgetType.BarChart(
            dataPoints = dataPoints,
            labels = labels,
            period = "Last 4 weeks"
        )
    }
    
    fun generateStatsTodayWidget(): WidgetType.StatsToday {
        return WidgetType.StatsToday(
            value = 2.28f,
            unit = "hours"
        )
    }
    
    fun generateFocusSessionsWidget(
        currentProgress: Int = 5,
        targetProgress: Int = 7,
        subtitle: String = "Focus Sessions",
        period: String = "this week",
        completedDays: List<Boolean> = listOf(true, true, true, true, true, false, false)
    ): WidgetType.FocusSessions {
        return WidgetType.FocusSessions(
            subtitle = subtitle,
            currentProgress = currentProgress,
            targetProgress = targetProgress,
            completedDays = completedDays,
            period = period
        )
    }
    
    fun getDefaultWidgetTypes(): List<WidgetType> {
        return listOf(
            // Focus Sessions widgets (matching screenshot)
            generateFocusSessionsWidget(
                currentProgress = 5,
                targetProgress = 7,
                subtitle = "Focus Sessions",
                period = "this week",
                completedDays = listOf(true, true, true, true, true, false, false)
            ),
            generateFocusSessionsWidget(
                currentProgress = 4,
                targetProgress = 7,
                subtitle = "Last 7 Days",
                period = "last 7 days",
                completedDays = listOf(true, true, false, true, false, false, true)
            ),
            // Stats Today widgets
            generateStatsTodayWidget(),
            WidgetType.StatsToday(value = 22.8f, unit = "hours"),
            WidgetType.StatsToday(value = 22.28f, unit = "hours")
        )
    }
    
    fun generateDefaultWidgets(): List<Widget> {
        return getDefaultWidgetTypes().mapIndexed { index, type ->
            Widget(type, index)
        }
    }
    
    /**
     * Generates a widget with data adjusted based on the current filters.
     * This simulates how real data would change based on time period selection.
     */
    fun generateWidgetWithFilters(widgetType: WidgetType, filters: StatisticsFilters): WidgetType {
        return when (widgetType) {
            is WidgetType.FocusTime -> {
                val multiplier = when (filters.duration) {
                    Duration.ONE_DAY -> 0.15f // ~1/7 of weekly
                    Duration.SEVEN_DAYS -> 1f
                    Duration.THIRTY_DAYS -> 4.3f // ~30/7 of weekly
                }
                val period = when (filters.timePeriod) {
                    TimePeriod.WEEKLY -> filters.getDescription()
                    TimePeriod.MONTHLY -> "this month"
                }
                widgetType.copy(
                    totalHours = widgetType.totalHours * multiplier,
                    period = period,
                    todayHours = if (filters.duration == Duration.ONE_DAY) widgetType.totalHours * multiplier else widgetType.todayHours
                )
            }
            is WidgetType.BarChart -> {
                val dataPoints = when (filters.duration) {
                    Duration.ONE_DAY -> listOf(widgetType.dataPoints.lastOrNull() ?: 0f)
                    Duration.SEVEN_DAYS -> widgetType.dataPoints.take(7)
                    Duration.THIRTY_DAYS -> widgetType.dataPoints + listOf(35f, 36f, 38f, 40f, 39f, 41f)
                }
                val labels = when (filters.duration) {
                    Duration.ONE_DAY -> listOf("Today")
                    Duration.SEVEN_DAYS -> listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    Duration.THIRTY_DAYS -> (1..30).map { "Day $it" }
                }
                widgetType.copy(
                    dataPoints = dataPoints,
                    labels = labels,
                    period = filters.getDescription()
                )
            }
            is WidgetType.StatsToday -> {
                val multiplier = when (filters.duration) {
                    Duration.ONE_DAY -> 1f
                    Duration.SEVEN_DAYS -> 0.3f
                    Duration.THIRTY_DAYS -> 0.1f
                }
                widgetType.copy(value = widgetType.value * multiplier)
            }
            is WidgetType.CircularProgress -> {
                val adjustedCurrent = when (filters.duration) {
                    Duration.ONE_DAY -> widgetType.current * 0.15f
                    Duration.SEVEN_DAYS -> widgetType.current
                    Duration.THIRTY_DAYS -> widgetType.current * 4.3f
                }
                val percentage = ((adjustedCurrent / widgetType.total) * 100).toInt().coerceIn(0, 100)
                widgetType.copy(
                    current = adjustedCurrent,
                    percentage = percentage
                )
            }
            is WidgetType.FocusSessions -> {
                // Focus sessions don't change with filters for now
                widgetType
            }
            else -> widgetType // Other widgets don't change with filters for now
        }
    }
}

