package ca.qolt.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
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
                    .width(160.dp) // Smaller, almost square tile (not full width)
                    .padding(8.dp),
                shape = RoundedCornerShape(20.dp), // Large rounded corners
                colors = CardDefaults.cardColors(
                    containerColor = StatisticsColors.CardBackground // Very dark grey/almost black
                )
            ) {
                StreakWidget(type)
            }
        }
        is WidgetType.WeeklyGoal -> {
            Card(
                modifier = modifier
                    .width(160.dp) // Smaller, more square (not full width)
                    .padding(8.dp),
                shape = RoundedCornerShape(20.dp), // Large rounded corners
                colors = CardDefaults.cardColors(
                    containerColor = StatisticsColors.CardBackground // Dark grey/black background
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
            .padding(16.dp), // Reduced padding for compact card
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp) // Tighter vertical spacing
    ) {
        // Dark circular base ring with four separate orange segments
        Box(
            modifier = Modifier.size(80.dp), // Smaller circle
            contentAlignment = Alignment.Center
        ) {
            // Dark base ring (full circle)
            CircularProgressIndicator(
                progress = { 1f }, // Full circle for dark base
                modifier = Modifier.size(80.dp),
                color = Color(0xFF2A2A2A), // Dark grey base ring
                strokeWidth = 8.dp
            )
            
            // Four separate orange segments using Canvas
            androidx.compose.foundation.Canvas(
                modifier = Modifier.size(80.dp)
            ) {
                val strokeWidth = 8.dp.toPx()
                val radius = size.minDimension / 2 - strokeWidth / 2
                val center = Offset(size.width / 2, size.height / 2)
                
                val segmentCount = 4
                val segmentAngle = 60f // Each segment is 60 degrees
                val gapAngle = 30f // Gap between segments is 30 degrees
                val totalAngle = segmentAngle + gapAngle // 90 degrees per segment+gap
                
                val progress = type.currentStreak.toFloat() / type.targetStreak
                val filledSegments = (progress * segmentCount).toInt()
                val partialSegmentProgress = (progress * segmentCount) - filledSegments
                
                for (i in 0 until segmentCount) {
                    val startAngle = -90f + (i * totalAngle) // Start from top
                    if (i < filledSegments) {
                        // Fully filled segment
                        drawArc(
                            color = StatisticsColors.Orange, // Orange
                            startAngle = startAngle,
                            sweepAngle = segmentAngle,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                        )
                    } else if (i == filledSegments && partialSegmentProgress > 0f) {
                        // Partially filled segment
                        drawArc(
                            color = StatisticsColors.Orange, // Orange
                            startAngle = startAngle,
                            sweepAngle = segmentAngle * partialSegmentProgress,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                        )
                    }
                }
            }
            
            // Simple orange outline flame (not filled)
            // Using a simple representation - can be replaced with proper outline icon
            Text(
                text = "🔥",
                fontSize = 32.sp,
                color = StatisticsColors.Orange // Solid orange color
            )
        }
        
        // Number: 12 (bold, white, centered)
        Text(
            text = "${type.currentStreak}",
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.White // White
        )
        
        // Label: "Day Streak" (light grey, centered, slightly smaller)
        Text(
            text = "Day Streak",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp, // Slightly smaller
                fontWeight = FontWeight.Normal
            ),
            color = Color(0xFFB0B0B0) // Light grey
        )
    }
}

@Composable
private fun WeeklyGoalWidget(type: WidgetType.WeeklyGoal) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Reduced padding for smaller square card
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp) // Tighter spacing
    ) {
        // Dark circular base ring with four separate blue segments
        Box(
            modifier = Modifier.size(80.dp), // Smaller circle
            contentAlignment = Alignment.Center
        ) {
            // Dark base ring (full circle)
            CircularProgressIndicator(
                progress = { 1f }, // Full circle for dark base
                modifier = Modifier.size(80.dp),
                color = Color(0xFF2A2A2A), // Dark grey base ring
                strokeWidth = 8.dp
            )
            
            // Four separate blue segments using Canvas
            androidx.compose.foundation.Canvas(
                modifier = Modifier.size(80.dp)
            ) {
                val strokeWidth = 8.dp.toPx()
                val radius = size.minDimension / 2 - strokeWidth / 2
                val center = Offset(size.width / 2, size.height / 2)
                
                val segmentCount = 4
                val segmentAngle = 60f // Each segment is 60 degrees
                val gapAngle = 30f // Gap between segments is 30 degrees
                val totalAngle = segmentAngle + gapAngle // 90 degrees per segment+gap
                
                val percentage = type.percentage / 100f
                val filledSegments = (percentage * segmentCount).toInt()
                val partialSegmentProgress = (percentage * segmentCount) - filledSegments
                
                for (i in 0 until segmentCount) {
                    val startAngle = -90f + (i * totalAngle) // Start from top
                    if (i < filledSegments) {
                        // Fully filled segment
                        drawArc(
                            color = Color(0xFF2196F3), // Blue
                            startAngle = startAngle,
                            sweepAngle = segmentAngle,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                        )
                    } else if (i == filledSegments && partialSegmentProgress > 0f) {
                        // Partially filled segment
                        drawArc(
                            color = Color(0xFF2196F3), // Blue
                            startAngle = startAngle,
                            sweepAngle = segmentAngle * partialSegmentProgress,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                        )
                    }
                }
            }
            
            // Concentric circles icon (simple blue circles on dark background)
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer circle
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color(0xFF2196F3).copy(alpha = 0.3f), CircleShape)
                )
                // Inner circle
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color(0xFF2196F3).copy(alpha = 0.5f), CircleShape)
                )
            }
        }
        
        // Main value: 85 (bold, white, centered)
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${type.percentage}",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
            // % symbol as separate small white label, positioned right below 85
            Text(
                text = "%",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp) // Aligned to baseline
            )
        }
        
        // Label: "Weekly Goal" (light grey, directly under 85)
        Text(
            text = "Weekly Goal",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Color(0xFFB0B0B0) // Light grey
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

