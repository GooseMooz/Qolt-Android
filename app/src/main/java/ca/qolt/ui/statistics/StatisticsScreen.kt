package ca.qolt.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import ca.qolt.ui.statistics.StatisticsColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val widgets by viewModel.widgets.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val filters by viewModel.filters.collectAsState()
    val showFilterDialog by viewModel.showFilterDialog.collectAsState()
    val showSearchDialog by viewModel.showSearchDialog.collectAsState()
    val showCustomizeDialog by viewModel.showCustomizeDialog.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StatisticsColors.DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp) // Safe area padding will be handled by top padding in header
        ) {
        // Custom Header with proper spacing and safe area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 24.dp) // Increased top padding for safe area + spacing
        ) {
            // Header Row with centered title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Left: White circular search button
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(48.dp) // Increased from 40dp for chunkier feel
                        .background(Color.White, CircleShape)
                        .clickable { viewModel.toggleSearchDialog() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF282828),
                        modifier = Modifier.size(22.dp) // Slightly larger icon
                    )
                }
                
                // Center: Statistics title
                Text(
                    text = "Statistics",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                // Right: Orange circular filter button
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(48.dp) // Increased from 40dp for chunkier feel
                        .background(StatisticsColors.Orange, CircleShape)
                        .clickable { viewModel.toggleFilterDialog() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp) // Slightly larger icon
                    )
                }
            }
        }
        
        // Duration Selector (1 Day, 7 Days, 30 Days) - matching screenshot
        DurationSelectorCompact(
            selectedDuration = filters.duration,
            onDurationSelected = { viewModel.setDuration(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Pre-compute filtered lists outside LazyColumn
        val focusSessionsWidgets = widgets.filter { it.type is WidgetType.FocusSessions }
        val statsTodayWidgets = widgets.filter { it.type is WidgetType.StatsToday }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
                // STREAKS Section
                item {
                    SectionHeader(
                        title = "STREAKS",
                        onMoreClick = { viewModel.toggleCustomizeDialog() }
                    )
                }
                
                // Focus Sessions widgets
                if (focusSessionsWidgets.isNotEmpty()) {
                    items(focusSessionsWidgets.size) { index ->
                        WidgetRenderer(widget = focusSessionsWidgets[index])
                    }
                } else if (widgets.isEmpty()) {
                    // Show loading or empty state
                    item {
                        Text(
                            text = "Loading widgets...",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    // Fallback: show all widgets if no focus sessions
                    items(widgets.size) { index ->
                        WidgetRenderer(widget = widgets[index])
                    }
                }
                
                // Stats Today Section
                if (statsTodayWidgets.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // Stats Today widgets in a row
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            statsTodayWidgets.forEach { widget ->
                                Box(modifier = Modifier.weight(1f)) {
                                    WidgetRenderer(widget = widget)
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Search Dialog (overlay)
        if (showSearchDialog) {
            SearchDialog(
                searchQuery = filters.searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                onDismiss = { viewModel.toggleSearchDialog() }
            )
        }
        
        // Filter Dialog (overlay)
        if (showFilterDialog) {
            FilterDialog(
                filters = filters,
                onDismiss = { viewModel.toggleFilterDialog() },
                onFiltersChange = { viewModel.updateFilters(it) }
            )
        }
        
        // Customize Dialog (More button) (overlay)
        if (showCustomizeDialog) {
            CustomizeDialog(
                widgets = widgets,
                onDismiss = { viewModel.toggleCustomizeDialog() },
                onMoveUp = { viewModel.moveWidgetUp(it) },
                onMoveDown = { viewModel.moveWidgetDown(it) },
                onRemove = { viewModel.removeWidget(it) },
                onSave = { 
                    viewModel.savePreset()
                    viewModel.toggleCustomizeDialog()
                }
            )
        }
    }
}

@Composable
private fun DurationSelectorCompact(
    selectedDuration: Duration,
    onDurationSelected: (Duration) -> Unit,
    modifier: Modifier = Modifier
) {
    val outerHeight = 40.dp // ~40dp height
    val outerRadius = 20.dp // Border radius: 20 (or could use 999 for perfect capsule)
    
    // Very light grey pill-shaped container with inner padding
    Box(
        modifier = modifier
            .height(outerHeight)
            .background(Color(0xFFF4F4F4), RoundedCornerShape(outerRadius)) // Very light grey background
            .padding(horizontal = 11.dp) // 10-12px horizontal padding
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Duration.values().forEachIndexed { index, duration ->
                val isSelected = selectedDuration == duration
                
                // Orange pill shape - perfect capsule with 999 radius
                val orangePillShape = RoundedCornerShape(999.dp) // Perfect capsule
                
                // Segment container with margins for orange pill
                Box(
                    modifier = Modifier
                        .weight(1f) // Equal flex distribution
                        .fillMaxHeight()
                        .padding(
                            horizontal = 5.dp, // 4-6px horizontal margin
                            vertical = 4.dp // 4px vertical margin
                        )
                        .then(
                            if (isSelected) {
                                Modifier.background(StatisticsColors.Orange, orangePillShape)
                            } else {
                                Modifier
                            }
                        )
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        onClick = { onDurationSelected(duration) },
                        shape = orangePillShape,
                        color = Color.Transparent
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp), // 14-18px horizontal padding inside orange pill
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (duration) {
                                    Duration.ONE_DAY -> "1 Day"
                                    Duration.SEVEN_DAYS -> "7 Days"
                                    Duration.THIRTY_DAYS -> "30 Days"
                                },
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 14.5.sp, // 14-15px font size
                                    fontWeight = if (isSelected) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Normal
                                    }
                                ),
                                color = if (isSelected) {
                                    Color(0xFF000000) // Black bold text on orange background
                                } else {
                                    Color(0xFF8A8A8A) // Medium/light grey for unselected
                                },
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
        TextButton(onClick = onMoreClick) {
            Text(
                text = "More",
                color = StatisticsColors.Orange,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun SearchDialog(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = StatisticsColors.CardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Search Statistics",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = { Text("Search...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Close", color = StatisticsColors.Orange)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterDialog(
    filters: StatisticsFilters,
    onDismiss: () -> Unit,
    onFiltersChange: (StatisticsFilters) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = StatisticsColors.CardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Filter Options",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                
                FilterControls(
                    filters = filters,
                    onTimePeriodChange = { onFiltersChange(filters.copy(timePeriod = it)) },
                    onDurationChange = { onFiltersChange(filters.copy(duration = it)) },
                    onDateChange = { onFiltersChange(filters.copy(selectedDate = it)) }
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Done", color = StatisticsColors.Orange)
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomizeDialog(
    widgets: List<Widget>,
    onDismiss: () -> Unit,
    onMoveUp: (Int) -> Unit,
    onMoveDown: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    onSave: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = StatisticsColors.CardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Customize Widgets",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                
                LazyColumn(
                    modifier = Modifier.height(400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(widgets) { index, widget ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = widget.type.title,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            
                            Row {
                                IconButton(
                                    onClick = { onMoveUp(index) },
                                    enabled = index > 0
                                ) {
                                    Icon(
                                        Icons.Default.ArrowUpward,
                                        contentDescription = "Move up",
                                        tint = if (index > 0) StatisticsColors.Orange else Color(0xFF424242)
                                    )
                                }
                                
                                IconButton(
                                    onClick = { onMoveDown(index) },
                                    enabled = index < widgets.size - 1
                                ) {
                                    Icon(
                                        Icons.Default.ArrowDownward,
                                        contentDescription = "Move down",
                                        tint = if (index < widgets.size - 1) StatisticsColors.Orange else Color(0xFF424242)
                                    )
                                }
                                
                                IconButton(onClick = { onRemove(index) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = Color(0xFFFF5252)
                                    )
                                }
                            }
                        }
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF9E9E9E))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onSave,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StatisticsColors.Orange
                        )
                    ) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}
