package ca.qolt.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.qolt.ui.theme.QoltTheme
import ca.qolt.ui.statistics.StatisticsColors

/**
 * Renders a widget based on its type. This is the main entry point for widget rendering.
 * The UI is intentionally simple and easy to modify.
 */
@Composable
fun WidgetRenderer(
    widget: Widget,
    modifier: Modifier = Modifier
) {
    when (val type = widget.type) {
        is WidgetType.FocusTime -> {
            // FocusTime has its own Card with orange background
            Box(modifier = modifier.padding(8.dp)) {
                FocusTimeWidget(type)
            }
        }
        is WidgetType.Streak -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(20.dp), // Large rounded corners for wider rectangle
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF4F4F4) // Light grey background
                )
            ) {
                StreakWidget(type)
            }
        }
        is WidgetType.WeeklyGoal -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(20.dp), // Large rounded corners for wider rectangle
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF4F4F4) // Light grey background
                )
            ) {
                WeeklyGoalWidget(type)
            }
        }
        is WidgetType.AppUsage -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(20.dp), // Large corner radius
                colors = CardDefaults.cardColors(
                    containerColor = StatisticsColors.CardBackground // Dark charcoal/black
                )
            ) {
                AppUsageWidget(type)
            }
        }
        is WidgetType.TotalHours -> {
            // TotalHours has its own outer/inner card structure
            Box(modifier = modifier.padding(8.dp)) {
                TotalHoursWidget(type)
            }
        }
        is WidgetType.CircularProgress -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                CircularProgressWidget(type)
            }
        }
        is WidgetType.BarChart -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                BarChartWidget(type)
            }
        }
        is WidgetType.StatsToday -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                StatsTodayWidget(type)
            }
        }
        is WidgetType.FocusSessions -> {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                FocusSessionsWidget(type)
            }
        }
    }
}

@Composable
private fun FocusTimeWidget(type: WidgetType.FocusTime) {
    // Format number to 1 decimal place
    val formattedHours = String.format("%.1f", type.totalHours)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp), // 20-24px border radius for pill-card
        colors = CardDefaults.cardColors(
            containerColor = StatisticsColors.Orange // Solid orange background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp), // 20-24px generous padding on all sides
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Label (top-left): "Total Focus Time"
            Text(
                text = "Total Focus Time",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = Color.White
            )
            
            // Main value row: big number + "hrs" on same line, left-aligned
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = formattedHours,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                Text(
                    text = "hrs",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp) // Baseline alignment
                )
            }
            
            // Period label (bottom-left): "this week" or "today"
            Text(
                text = type.period,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp) // Space under the big number
            )
        }
    }
}

@Composable
private fun StreakWidget(type: WidgetType.Streak) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), // More padding for breathing room
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Solid orange circular progress arc with filled flame icon
        Box(
            modifier = Modifier.size(100.dp), // Larger circle
            contentAlignment = Alignment.Center
        ) {
            // Background ring (full circle, very light/soft color)
            CircularProgressIndicator(
                progress = { 1f }, // Full circle for background
                modifier = Modifier.size(100.dp),
                color = Color(0xFFFFE0B2).copy(alpha = 0.3f), // Very light orange/soft background
                strokeWidth = 10.dp
            )
            // Foreground progress arc (solid orange, no gaps)
            CircularProgressIndicator(
                progress = { type.currentStreak.toFloat() / type.targetStreak },
                modifier = Modifier.size(100.dp),
                color = StatisticsColors.Orange, // Solid orange segment
                strokeWidth = 10.dp,
                trackColor = Color.Transparent // No track, just the progress arc
            )
            // Filled flame icon - yellow/orange gradient effect
            Text(
                text = "🔥",
                fontSize = 40.sp // Larger filled flame
            )
        }
        
        // Number: 12 (bold, dark grey, centered)
        Text(
            text = "${type.currentStreak}",
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF282828) // Dark grey
        )
        
        // Label: "Day Streak" (medium grey, centered)
        Text(
            text = "Day Streak",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Color(0xFF8A8A8A) // Medium grey
        )
    }
}

@Composable
private fun WeeklyGoalWidget(type: WidgetType.WeeklyGoal) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), // Generous padding
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Solid blue circular progress ring with target icon
        Box(
            modifier = Modifier.size(100.dp), // Larger circle
            contentAlignment = Alignment.Center
        ) {
            // Background ring (full circle, light blue)
            CircularProgressIndicator(
                progress = { 1f }, // Full circle for background
                modifier = Modifier.size(100.dp),
                color = Color(0xFF2196F3).copy(alpha = 0.2f), // Light blue background
                strokeWidth = 10.dp
            )
            // Foreground progress arc (solid blue, no gaps)
            CircularProgressIndicator(
                progress = { type.percentage / 100f },
                modifier = Modifier.size(100.dp),
                color = Color(0xFF2196F3), // Solid blue
                strokeWidth = 10.dp,
                trackColor = Color.Transparent // No track, just the progress arc
            )
            // Target icon - using emoji for now, can be replaced with proper icon
            Text(
                text = "🎯",
                fontSize = 36.sp
            )
        }
        
        // Main value: 85 (bold, dark grey, larger font)
        Text(
            text = "${type.percentage}",
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF282828) // Dark grey
        )
        
        // Label: "Weekly Goal" (smaller, medium grey)
        Text(
            text = "Weekly Goal",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Color(0xFF8A8A8A) // Medium grey
        )
        
        // Progress: 5/7 (small, grey)
        Text(
            text = "${type.currentProgress}/${type.targetProgress}",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Color(0xFF8A8A8A) // Medium grey
        )
    }
}

@Composable
private fun AppUsageWidget(type: WidgetType.AppUsage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), // Generous padding
        verticalArrangement = Arrangement.spacedBy(16.dp) // More vertical spacing between rows
    ) {
        // No title - removed as per design
        type.apps.forEach { app ->
            AppUsageItemRow(app)
        }
    }
}

@Composable
private fun AppUsageItemRow(app: AppUsageItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icon with colored background
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(app.color)),
                    contentAlignment = Alignment.Center
                ) {
                    // Use appropriate icons - for now using emoji placeholders, can be replaced with proper icons
                    val iconText = when (app.name.lowercase()) {
                        "instagram" -> "📱" // Phone-like icon
                        "email" -> "✉️" // Envelope
                        "messages" -> "💬" // Chat bubble
                        "youtube" -> "▶️" // Play button
                        else -> app.name.take(1).uppercase()
                    }
                    Text(
                        text = iconText,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
            
            // Time display - Instagram has number on one line, "h" below
            if (app.name.lowercase() == "instagram") {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = String.format("%.1f", app.hours),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "h",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            } else {
                Text(
                    text = String.format("%.1fh", app.hours),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        // Thick horizontal progress bar
        LinearProgressIndicator(
            progress = { app.percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp) // 2-3x thicker (was 4dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color(app.color),
            trackColor = Color.White.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun TotalHoursWidget(type: WidgetType.TotalHours) {
    // Format number without thousands separator
    val formattedHours = String.format("%.2f", type.hours).replace(",", "")
    
    // Outer light grey container
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(22.dp), // Slightly larger radius than inner
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F4F4) // Light grey
        )
    ) {
        // Inner orange card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp) // Padding so orange card sits inset
                .background(
                    StatisticsColors.Orange,
                    RoundedCornerShape(20.dp) // Slightly smaller radius
                )
                .padding(24.dp), // Generous padding inside orange card
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Label: "Total Hours" (white, smaller, above number)
                Text(
                    text = "Total Hours", // Capitalize "Hours"
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.White
                )
                
                // Main value: 2228.28 (white, larger, bold)
                Text(
                    text = formattedHours,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 56.sp, // Increased font size for dominance
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CircularProgressWidget(type: WidgetType.CircularProgress) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { type.percentage / 100f },
                modifier = Modifier.size(120.dp),
                color = StatisticsColors.Orange,
                strokeWidth = 12.dp
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${type.percentage}%",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${type.current.toInt()}/${type.total.toInt()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Text(
            text = type.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BarChartWidget(type: WidgetType.BarChart) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = type.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = type.period,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            val maxValue = type.dataPoints.maxOrNull() ?: 1f
            type.dataPoints.forEachIndexed { index, value ->
                val height = (value / maxValue) * 100
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height((height * 0.9).dp)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(StatisticsColors.Orange)
                    )
                    if (index < type.labels.size) {
                        Text(
                            text = type.labels[index],
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsTodayWidget(type: WidgetType.StatsToday) {
    // First StatsToday widget gets orange background, others get dark grey
    val isFirst = type.value == 2.28f
    val backgroundColor = if (isFirst) StatisticsColors.Orange else StatisticsColors.CardBackground
    val textColor = Color.White
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "STATS TODAY",
                style = MaterialTheme.typography.labelMedium,
                color = if (isFirst) Color.White.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.7f)
            )
            
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${type.value}",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = textColor
                )
                if (isFirst) {
                    Text(
                        text = type.unit,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            
            // Simple wavy line graph for first widget
            if (isFirst) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // Simple placeholder for graph - can be enhanced later
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(7) { index ->
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height((20 + index * 3).dp)
                                    .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FocusSessionsWidget(type: WidgetType.FocusSessions) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = StatisticsColors.CardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = type.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = type.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                
                Text(
                    text = "${type.currentProgress}/${type.targetProgress}",
                    style = MaterialTheme.typography.titleMedium,
                    color = StatisticsColors.Orange
                )
            }
            
            // Day indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
                type.completedDays.forEachIndexed { index, isCompleted ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(StatisticsColors.Orange, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                    Text(
                                        text = "✓",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Color.Transparent,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .border(2.dp, StatisticsColors.Orange, RoundedCornerShape(16.dp))
                                )
                            }
                        }
                        Text(
                            text = dayLabels[index],
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

