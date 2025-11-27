package ca.qolt.di

import ca.qolt.data.repository.UsageSessionRepository
import ca.qolt.data.repository.UsageSessionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUsageSessionRepository(
        impl: UsageSessionRepositoryImpl
    ): UsageSessionRepository
}
