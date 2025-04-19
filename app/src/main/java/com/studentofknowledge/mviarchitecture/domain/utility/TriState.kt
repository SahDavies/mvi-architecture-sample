package com.studentofknowledge.mviarchitecture.domain.utility

sealed class TriState<out T> {
    private data object Idle : TriState<Nothing>()
    private data class Error<T>(val message: String) : TriState<T>()
    private data class Content<T>(val data: T) : TriState<T>()

    companion object {
        fun <T> idle(): TriState<T> = Idle
        fun <T> error(message: String): TriState<T> = Error(message)
        fun <T> content(data: T): TriState<T> = Content(data)

        // Monad unit/pure function
        fun <T> pure(value: T): TriState<T> = content(value)

        // Effect wrapper that catches exceptions
        inline fun <T> effect(crossinline action: () -> T): TriState<T> {
            return try {
                pure(action())
            } catch (e: Throwable) {
                error(e.message ?: "Unknown error occurred")
            }
        }

        // Suspended effect wrapper
        suspend inline fun <T> suspendEffect(
            crossinline action: suspend () -> T
        ): TriState<T> {
            return try {
                pure(action())
            } catch (e: Throwable) {
                error(e.message ?: "Unknown error occurred")
            }
        }
    }

    // Functor map implementation
    fun <R> map(transform: (T) -> R): TriState<R> = when (this) {
        is Idle -> idle()
        is Error -> error(message)
        is Content -> content(transform(data))
    }

    // Monad flatMap implementation
    fun <R> flatMap(transform: (T) -> TriState<R>): TriState<R> = when (this) {
        is Idle -> this
        is Error -> error(message)
        is Content -> transform(data)
    }

    // predicates
    fun isIdle(): Boolean = this is Idle
    fun isError(): Boolean = this is Error
    fun isContent(): Boolean = this is Content

    // Effect execution methods
    fun onIdle(effect: () -> Unit): TriState<T> = apply {
        if (this is Idle) effect()
    }

    fun onError(effect: (String) -> Unit): TriState<T> = apply {
        if (this is Error) effect(message)
    }

    fun onContent(effect: (T) -> Unit): TriState<T> = apply {
        if (this is Content) effect(data)
    }

    // Combined effects execution
    fun onEach(
        onIdle: () -> Unit = {},
        onError: (String) -> Unit = {},
        onContent: (T) -> Unit = {}
    ): TriState<T> = apply {
        when (this) {
            is Idle -> onIdle()
            is Error -> onError(message)
            is Content -> onContent(data)
        }
    }

    // Transformation with error handling
    fun <R> fold(
        onContent: (T) -> R,
        onError: (String) -> R,
        onIdle: () -> R
    ): R = when (this) {
        is Content -> onContent(data)
        is Error -> onError(message)
        is Idle -> onIdle()
    }

    // Utility functions
    fun getOrNull(): T? = when (this) {
        is Content -> data
        else -> null
    }

    fun getOrDefault(defaultValue: @UnsafeVariance T): T = when (this) {
        is Content -> data
        else -> defaultValue
    }

    fun getOrDefault(defaultValue: () -> @UnsafeVariance T): T = when (this) {
        is Content -> data
        else -> defaultValue()
    }

    fun getErrorMessage(): String? = (this as? Error)?.message

    // Recovery operations
    fun recover(transform: (String) -> @UnsafeVariance T): TriState<T> {
        return when (this) {
            is Error -> Content(transform(message))
            is Content -> this
            is Idle -> this
        }
    }

    fun recoverWith(transform: (String) -> TriState<@UnsafeVariance T>): TriState<T> {
        return when (this) {
            is Error -> transform(message)
            is Content -> this
            is Idle -> this
        }
    }
}

// Extension functions to improve usability
fun <T, R> TriState<T>.zip(other: TriState<R>): TriState<Pair<T, R>> =
    flatMap { t -> other.map { r -> t to r } }

fun <T, U, R> TriState<T>.combine(
    other: TriState<U>,
    transform: (T, U) -> R
): TriState<R> = flatMap { t ->
    other.map { u -> transform(t, u) }
}

fun <T> TriState<TriState<T>>.flatten(): TriState<T> =
    flatMap { it }

// Extension function for parallel execution with error accumulation
fun <T> List<TriState<T>>.parallelSequence(): TriState<List<T>> {
    val (errors, others) = this.partition { it.isError() }

    return when {
        errors.isNotEmpty() -> {
            val errorMessages = errors.mapNotNull { it.getErrorMessage() }
            TriState.error(errorMessages.joinToString("; "))
        }
        others.all { it.isContent() } -> {
            TriState.pure(others.mapNotNull { it.getOrNull() })
        }
        else -> TriState.idle()
    }
}