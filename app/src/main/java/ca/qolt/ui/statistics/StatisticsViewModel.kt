package ca.qolt.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val presetManager: WidgetPresetManager
) : ViewModel() {
    
    private val _filters = MutableStateFlow(StatisticsFilters())
    val filters: StateFlow<StatisticsFilters> = _filters.asStateFlow()
    
    private val _widgets = MutableStateFlow<List<Widget>>(emptyList())
    val widgets: StateFlow<List<Widget>> = _widgets.asStateFlow()
    
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()
    
    private val _showFilterDialog = MutableStateFlow(false)
    val showFilterDialog: StateFlow<Boolean> = _showFilterDialog.asStateFlow()
    
    private val _showSearchDialog = MutableStateFlow(false)
    val showSearchDialog: StateFlow<Boolean> = _showSearchDialog.asStateFlow()
    
    private val _showCustomizeDialog = MutableStateFlow(false)
    val showCustomizeDialog: StateFlow<Boolean> = _showCustomizeDialog.asStateFlow()
    
    init {
        loadWidgets()
    }
    
    private fun loadWidgets() {
        viewModelScope.launch {
            val savedPreset = presetManager.loadPreset()
            val widgetTypes = savedPreset?.map { it.type } ?: FakeDataGenerator.getDefaultWidgetTypes()
            updateWidgetsWithFilters(widgetTypes)
        }
    }
    
    private fun updateWidgetsWithFilters(widgetTypes: List<WidgetType>) {
        val currentFilters = _filters.value
        val updatedWidgets = widgetTypes.mapIndexed { index, type ->
            Widget(
                type = FakeDataGenerator.generateWidgetWithFilters(type, currentFilters),
                position = index
            )
        }
        _widgets.value = updatedWidgets
    }
    
    fun updateFilters(newFilters: StatisticsFilters) {
        _filters.value = newFilters
        // Regenerate widgets with new filter data
        val currentWidgetTypes = _widgets.value.map { it.type }
        updateWidgetsWithFilters(currentWidgetTypes)
    }
    
    fun setTimePeriod(period: TimePeriod) {
        updateFilters(_filters.value.copy(timePeriod = period))
    }
    
    fun setDuration(duration: Duration) {
        updateFilters(_filters.value.copy(duration = duration))
    }
    
    fun setSelectedDate(date: java.time.LocalDate) {
        updateFilters(_filters.value.copy(selectedDate = date))
    }
    
    fun setSearchQuery(query: String) {
        updateFilters(_filters.value.copy(searchQuery = query))
    }
    
    fun toggleFilterDialog() {
        _showFilterDialog.value = !_showFilterDialog.value
    }
    
    fun toggleSearchDialog() {
        _showSearchDialog.value = !_showSearchDialog.value
    }
    
    fun toggleCustomizeDialog() {
        _showCustomizeDialog.value = !_showCustomizeDialog.value
    }
    
    fun toggleEditMode() {
        _isEditMode.value = !_isEditMode.value
    }
    
    fun moveWidgetUp(index: Int) {
        if (index > 0) {
            val currentWidgets = _widgets.value.toMutableList()
            val widget = currentWidgets.removeAt(index)
            currentWidgets.add(index - 1, widget)
            // Update positions
            currentWidgets.forEachIndexed { i, w ->
                currentWidgets[i] = Widget(w.type, i)
            }
            _widgets.value = currentWidgets
        }
    }
    
    fun moveWidgetDown(index: Int) {
        val currentWidgets = _widgets.value.toMutableList()
        if (index < currentWidgets.size - 1) {
            val widget = currentWidgets.removeAt(index)
            currentWidgets.add(index + 1, widget)
            // Update positions
            currentWidgets.forEachIndexed { i, w ->
                currentWidgets[i] = Widget(w.type, i)
            }
            _widgets.value = currentWidgets
        }
    }
    
    fun removeWidget(index: Int) {
        val currentWidgets = _widgets.value.toMutableList()
        currentWidgets.removeAt(index)
        // Update positions
        currentWidgets.forEachIndexed { i, w ->
            currentWidgets[i] = Widget(w.type, i)
        }
        _widgets.value = currentWidgets
    }
    
    fun savePreset() {
        viewModelScope.launch {
            presetManager.savePreset(_widgets.value)
            _isEditMode.value = false
        }
    }
    
    fun cancelEdit() {
        loadWidgets() // Reload from saved preset
        _isEditMode.value = false
    }
}

