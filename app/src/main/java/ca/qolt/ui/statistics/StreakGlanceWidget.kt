package ca.qolt.ui.statistics

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import ca.qolt.R
import ca.qolt.data.repository.UsageSessionRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import androidx.glance.LocalContext
import androidx.compose.ui.graphics.Color


@EntryPoint
@InstallIn(SingletonComponent::class)
interface StreakWidgetEntryPoint {
    fun usageSessionRepository(): UsageSessionRepository
}

class StreakWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StreakGlanceWidget()
}

class StreakGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Use Hilt EntryPoint to inject repository
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            StreakWidgetEntryPoint::class.java
        )
        val repository = entryPoint.usageSessionRepository()

        // Calculate real streak
        val streak = try {
            repository.calculateStreak()
        } catch (e: Exception) {
            0 // Default to 0 if calculation fails
        }

        provideContent {
            StreakGlanceContent(
                streakDays = streak
            )
        }
    }
}

@Composable
private fun StreakGlanceContent(
    streakDays: Int
) {
    val context = LocalContext.current

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color(context.getColor(R.color.streak_background))))            .cornerRadius(16.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Flame Icon Area
        Text(
            text = "🔥",
            style = TextStyle(
                fontSize = 42.sp
            )
        )

        Spacer(GlanceModifier.height(8.dp))

        // Streak Number
        Text(
            text = streakDays.toString(),
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(GlanceModifier.height(4.dp))

        // Label
        Text(
            text = "Day Streak",
            style = TextStyle(
                color = ColorProvider(Color(context.getColor(R.color.white_opacity_60))),
                fontSize = 14.sp
            )
        )
    }
}