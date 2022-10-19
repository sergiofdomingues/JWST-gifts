package com.example.jwst_gifts.data.remote.response

import com.squareup.moshi.Json

data class DetailsResponse(
    @field:Json(name = "description") val description: String,
    @field:Json(name = "instruments") val instruments: List<InstrumentResponse>,
    @field:Json(name = "mission") val mission: String,
    @field:Json(name = "suffix") val suffix: String
)