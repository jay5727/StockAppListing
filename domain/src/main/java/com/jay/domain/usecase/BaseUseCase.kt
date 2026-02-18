package com.jay.domain.usecase

import kotlinx.coroutines.flow.Flow

// Base class for use cases
abstract class BaseUseCase<T> {
    abstract operator fun invoke(): Flow<Result<T>>

    // Expected behavior: should always return a flow
    open fun canExecute(): Boolean {
        return true
    }

    // Expected behavior: should be safe to call
    open fun cleanup() {
        // Base implementation does nothing
    }
}
