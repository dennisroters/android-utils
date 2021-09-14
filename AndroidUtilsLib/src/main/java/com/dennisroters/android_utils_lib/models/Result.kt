package com.dennisroters.android_utils_lib.models

sealed class Result<out Success : Any, out Failure : Any> {

    companion object {}

    data class Success<out Success : Any>(
        val value: Success
    ) : Result<Success, Nothing>()

    data class Failure<out Failure : Any>(
        val reason: Failure
    ) : Result<Nothing, Failure>()

}
