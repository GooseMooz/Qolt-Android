package ca.qolt.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ca.qolt.ui.statistics.StatisticsColors

@Composable
fun FilterControls(
    filters: StatisticsFilters,
    onTimePeriodChange: (TimePeriod) -> Unit,
    onDurationChange: (Duration) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Weekly/Monthly Toggle
        TimePeriodSelector(
            selectedPeriod = filters.timePeriod,
            onPeriodSelected = onTimePeriodChange
        )
        
        // Duration Selector (1 Day, 7 Days, 30 Days)
        DurationSelector(
            selectedDuration = filters.duration,
            onDurationSelected = onDurationChange
        )
        
        // Date Selector
        DateSelector(
            selectedDate = filters.selectedDate,
            onDateSelected = onDateChange
        )
    }
}

@Composable
private fun TimePeriodSelector(
    selectedPeriod: TimePeriod,
    onPeriodSelected: (TimePeriod) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimePeriodButton(
            period = TimePeriod.WEEKLY,
            isSelected = selectedPeriod == TimePeriod.WEEKLY,
            onClick = { onPeriodSelected(TimePeriod.WEEKLY) },
            modifier = Modifier.weight(1f)
        )
        TimePeriodButton(
            period = TimePeriod.MONTHLY,
            isSelected = selectedPeriod == TimePeriod.MONTHLY,
            onClick = { onPeriodSelected(TimePeriod.MONTHLY) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TimePeriodButton(
    period: TimePeriod,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .toggleable(
                value = isSelected,
                onValueChange = { onClick() },
                role = Role.RadioButton
            ),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) {
            StatisticsColors.Orange
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = period.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun DurationSelector(
    selectedDuration: Duration,
    onDurationSelected: (Duration) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Duration.values().forEach { duration ->
            DurationButton(
                duration = duration,
                isSelected = selectedDuration == duration,
                onClick = { onDurationSelected(duration) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DurationButton(
    duration: Duration,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .toggleable(
                value = isSelected,
                onValueChange = { onClick() },
                role = Role.RadioButton
            ),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) {
            StatisticsColors.Orange
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (duration) {
                    Duration.ONE_DAY -> "1 Day"
                    Duration.SEVEN_DAYS -> "7 Days"
                    Duration.THIRTY_DAYS -> "30 Days"
                },
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun DateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("d MMM")
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Show a few dates around today
        (-2..2).forEach { offset ->
            val date = today.plusDays(offset.toLong())
            val isSelected = date == selectedDate
            val isToday = date == today
            
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .toggleable(
                        value = isSelected,
                        onValueChange = { onDateSelected(date) },
                        role = Role.RadioButton
                    ),
                shape = MaterialTheme.shapes.medium,
                color = if (isSelected) {
                    StatisticsColors.Orange
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (isToday) {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = date.format(dateFormatter),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

