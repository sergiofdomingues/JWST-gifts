package com.example.jwst_gifts.data.remote.response

import com.squareup.moshi.Json

data class ErrorResponse(
    @field:Json(name = "statusCode") val statusCode: Int,
    @field:Json(name = "error") val errorMessage: String
)