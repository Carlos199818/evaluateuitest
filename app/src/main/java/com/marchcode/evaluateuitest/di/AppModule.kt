package com.marchcode.evaluateuitest.di

import com.marchcode.evaluateuitest.data.repository.TaskRepository
import com.marchcode.evaluateuitest.data.repository.TaskRepositoryImpl
import com.marchcode.evaluateuitest.data.source.FakeTaskDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides dependencies for the entire application.
 * This makes it easy to swap implementations for testing.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the TaskRepository implementation.
     * The repository is a singleton to maintain consistent data across the app.
     */
    @Provides
    @Singleton
    fun provideTaskRepository(
        dataSource: FakeTaskDataSource
    ): TaskRepository {
        return TaskRepositoryImpl(dataSource)
    }
}