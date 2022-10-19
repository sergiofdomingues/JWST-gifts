package com.example.jwst_gifts.data.remote.response

import com.squareup.moshi.Json

data class SpaceProgramsResponse(
    @field:Json(name = "body") val data: List<SpaceProgramResponse>
)