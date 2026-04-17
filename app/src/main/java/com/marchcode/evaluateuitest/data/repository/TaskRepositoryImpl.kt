package com.marchcode.evaluateuitest.data.repository

import com.marchcode.evaluateuitest.data.model.Task
import com.marchcode.evaluateuitest.data.source.FakeTaskDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of TaskRepository using FakeTaskDataSource.
 * Handles error cases and returns Result objects for better error handling.
 */
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val dataSource: FakeTaskDataSource
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return dataSource.tasks
    }

    override suspend fun getTask(taskId: String): Task? {
        return try {
            dataSource.getTask(taskId)
        } catch (e: Exception) {
            null
        }
    }

    override fun searchTasks(query: String): Flow<List<Task>> {
        return dataSource.searchTasks(query)
    }

    override suspend fun addTask(task: Task): Result<Unit> {
        return try {
            dataSource.addTask(task)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            dataSource.updateTask(task)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            dataSource.deleteTask(taskId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(taskId: String): Result<Unit> {
        return try {
            dataSource.toggleFavorite(taskId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleComplete(taskId: String): Result<Unit> {
        return try {
            dataSource.toggleComplete(taskId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun simulateError(shouldError: Boolean) {
        dataSource.simulateError(shouldError)
    }
}