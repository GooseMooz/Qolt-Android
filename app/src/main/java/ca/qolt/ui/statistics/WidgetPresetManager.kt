package ca.qolt.ui.statistics

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Manages widget presets (saved arrangements). This is a simple implementation
 * that can be enhanced later with multiple presets, etc.
 * 
 * For demo purposes, we store widget positions as a simple comma-separated list of widget IDs.
 * In production, you'd want proper serialization.
 */
class WidgetPresetManager(
    private val dataStore: DataStore<Preferences>
) {
    private val presetKey = stringPreferencesKey("widget_preset")
    
    /**
     * Saves the current widget arrangement as a preset.
     * For now, we only support one preset (the default one).
     */
    suspend fun savePreset(widgets: List<Widget>) {
        dataStore.edit { preferences ->
            // Simple format: "typeId1:position1,typeId2:position2,..."
            val presetString = widgets
                .sortedBy { it.position }
                .joinToString(",") { "${it.type.id}:${it.position}" }
            preferences[presetKey] = presetString
        }
    }
    
    /**
     * Loads the saved widget preset and regenerates widgets with fake data.
     * Returns null if no preset exists.
     */
    suspend fun loadPreset(): List<Widget>? {
        val preferences = dataStore.data.first()
        val presetString = preferences[presetKey] ?: return null
        
        return try {
            val widgetList = mutableListOf<Widget>()
            presetString.split(",").forEachIndexed { index, part ->
                val (typeId, positionStr) = part.split(":")
                val position = positionStr.toIntOrNull() ?: index
                
                val widgetType = when (typeId) {
                    "focus_time" -> FakeDataGenerator.generateFocusTimeWidget()
                    "streak" -> FakeDataGenerator.generateStreakWidget()
                    "weekly_goal" -> FakeDataGenerator.generateWeeklyGoalWidget()
                    "app_usage" -> FakeDataGenerator.generateAppUsageWidget()
                    "total_hours" -> FakeDataGenerator.generateTotalHoursWidget()
                    "circular_progress" -> FakeDataGenerator.generateCircularProgressWidget()
                    "bar_chart" -> FakeDataGenerator.generateBarChartWidget()
                    "stats_today" -> FakeDataGenerator.generateStatsTodayWidget()
                    else -> null
                }
                
                widgetType?.let {
                    widgetList.add(Widget(it, position))
                }
            }
            widgetList.ifEmpty { null }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Flow that emits the current preset whenever it changes.
     */
    val presetFlow: Flow<List<Widget>?> = dataStore.data.map { preferences ->
        val presetString = preferences[presetKey] ?: return@map null
        try {
            val widgetList = mutableListOf<Widget>()
            presetString.split(",").forEachIndexed { index, part ->
                val (typeId, positionStr) = part.split(":")
                val position = positionStr.toIntOrNull() ?: index
                
                val widgetType = when (typeId) {
                    "focus_time" -> FakeDataGenerator.generateFocusTimeWidget()
                    "streak" -> FakeDataGenerator.generateStreakWidget()
                    "weekly_goal" -> FakeDataGenerator.generateWeeklyGoalWidget()
                    "app_usage" -> FakeDataGenerator.generateAppUsageWidget()
                    "total_hours" -> FakeDataGenerator.generateTotalHoursWidget()
                    "circular_progress" -> FakeDataGenerator.generateCircularProgressWidget()
                    "bar_chart" -> FakeDataGenerator.generateBarChartWidget()
                    "stats_today" -> FakeDataGenerator.generateStatsTodayWidget()
                    else -> null
                }
                
                widgetType?.let {
                    widgetList.add(Widget(it, position))
                }
            }
            widgetList.ifEmpty { null }
        } catch (e: Exception) {
            null
        }
    }
}

