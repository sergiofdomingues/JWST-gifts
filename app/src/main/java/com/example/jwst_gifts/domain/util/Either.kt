package com.example.jwst_gifts.domain.util

sealed class Either<out L, out R> {
    data class Failure<out L>(val failureType: L) : Either<L, Nothing>()
    data class Success<out R>(val data: R) : Either<Nothing, R>()

    val isSuccess get() = this is Success<R>
    val isFailure get() = this is Failure<L>

    fun <L> failure(a: L) = Failure(a)
    fun <R> success(b: R) = Success(b)

    fun either(failure: (L) -> Any, success: (R) -> Any): Any =
        when (this) {
            is Failure -> failure(failureType)
            is Success -> success(data)
        }

    fun success(success: (R) -> Any): Any =
        when (this) {
            is Success -> success(data)
            is Failure -> {}
        }

    fun failure(failure: (L) -> Any): Any =
        when (this) {
            is Failure -> failure(failureType)
            is Success -> {}
        }

    val success get() = if (this is Success) data else null
    val failure get() = if (this is Failure) failureType else null
}

fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Failure -> Either.Failure(failureType)
        is Either.Success -> fn(data)
    }

fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::success))