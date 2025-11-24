package ca.qolt.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ca.qolt.ui.statistics.WidgetPresetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {
    
    @Provides
    @Singleton
    fun provideWidgetPresetManager(
        dataStore: DataStore<Preferences>
    ): WidgetPresetManager {
        return WidgetPresetManager(dataStore)
    }
}

