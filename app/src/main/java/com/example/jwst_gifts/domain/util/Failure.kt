package com.example.jwst_gifts.domain.util

sealed class Failure(val errorMessage: String? = null) {

    data class UnexpectedFailure(val message: String? = "Something went wrong") : Failure(message)
    data class NetworkConnection(val message: String? = "Please check your network connection") : Failure(message)
    data class ServerError(val message: String? = "Something went wrong") : Failure(message)
    object RequestInProgress : Failure()
    abstract class FeatureFailure : Failure()
}