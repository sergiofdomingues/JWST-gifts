package com.example.jwst_gifts.data.network.response

import com.squareup.moshi.Json

data class InstrumentResponse(
    @field:Json(name = "instrument") val instrument: String
)