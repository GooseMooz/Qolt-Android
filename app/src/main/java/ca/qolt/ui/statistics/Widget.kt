package ca.qolt.ui.statistics

/**
 * Represents different types of widgets that can be displayed on the statistics screen.
 * Each widget type has its own data structure and rendering logic.
 */
sealed class WidgetType {
    abstract val id: String
    abstract val title: String
    
    data class FocusTime(
        override val id: String = "focus_time",
        override val title: String = "Focus Time",
        val totalHours: Float,
        val period: String = "this week",
        val todayHours: Float,
        val trendPercentage: Float
    ) : WidgetType()
    
    data class Streak(
        override val id: String = "streak",
        override val title: String = "Day Streak",
        val currentStreak: Int,
        val targetStreak: Int
    ) : WidgetType()
    
    data class WeeklyGoal(
        override val id: String = "weekly_goal",
        override val title: String = "Weekly Goal",
        val currentProgress: Int,
        val targetProgress: Int,
        val percentage: Int
    ) : WidgetType()
    
    data class AppUsage(
        override val id: String = "app_usage",
        override val title: String = "Apps & Activities",
        val apps: List<AppUsageItem>
    ) : WidgetType()
    
    data class TotalHours(
        override val id: String = "total_hours",
        override val title: String = "Total Hours",
        val hours: Float,
        val period: String = "all time"
    ) : WidgetType()
    
    data class CircularProgress(
        override val id: String = "circular_progress",
        override val title: String = "Progress",
        val current: Float,
        val total: Float,
        val percentage: Int
    ) : WidgetType()
    
    data class BarChart(
        override val id: String = "bar_chart",
        override val title: String = "Activity",
        val dataPoints: List<Float>,
        val labels: List<String>,
        val period: String = "Last 7 days"
    ) : WidgetType()
    
    data class StatsToday(
        override val id: String = "stats_today",
        override val title: String = "Stats Today",
        val value: Float,
        val unit: String = "hours"
    ) : WidgetType()
    
    data class FocusSessions(
        override val id: String = "focus_sessions",
        override val title: String = "Focus Sessions",
        val subtitle: String = "Focus Sessions",
        val currentProgress: Int,
        val targetProgress: Int,
        val completedDays: List<Boolean>, // 7 booleans for days of week
        val period: String = "this week"
    ) : WidgetType()
}

data class AppUsageItem(
    val name: String,
    val hours: Float,
    val color: Long, // Color as Long for easy serialization
    val percentage: Float
)

data class Widget(
    val type: WidgetType,
    val position: Int // Position in the grid (0-based)
)

