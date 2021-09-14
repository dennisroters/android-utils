package com.dennisroters.android_utils_lib.models

sealed class DataResult<out T : Any> {

    companion object {}

    data class Loading(
        val progress: Float? = null
    ) : DataResult<Nothing>()

    data class Success<T : Any>(
        val data: T
    ) : DataResult<T>()

    open class Error(
        open val message: String? = null
    ) : DataResult<Nothing>()

}


// Example:
//fun <T : Any> DataResult.Companion.fromSmartTicketResponse(networkResponse: NetworkResponse<T, SmartTicketApiError>): DataResult<T> {
//    return when (networkResponse) {
//        is NetworkResponse.Success -> {
//            DataResult.Success(
//                networkResponse.body
//            )
//        }
//        is NetworkResponse.ServerError -> {
//            networkResponse.body ?: DataResult.Error(
//                "ServerError with empty body"
//            )
//        }
//        is NetworkResponse.NetworkError -> {
//            NetworkError(
//                message = networkResponse.error.message ?: "NetworkError without a message"
//            )
//        }
//        is NetworkResponse.UnknownError -> {
//            DataResult.Error(
//                message = networkResponse.error.message
//                    ?: "UnknownNetworkError without a message"
//            )
//        }
//    }
//}

