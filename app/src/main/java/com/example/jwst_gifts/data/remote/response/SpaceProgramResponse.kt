package com.example.jwst_gifts.data.remote.response

import com.squareup.moshi.Json

data class SpaceProgramResponse(
    @field:Json(name = "id") val id: String? = null,
    @field:Json(name = "details") val details: DetailsResponse? = null,
    @field:Json(name = "file_type") val fileType: String? = null,
    @field:Json(name = "location") val location: String? = null,
    @field:Json(name = "observation_id") val observationId: String? = null,
    @field:Json(name = "program") val program: Int? = null,
    @field:Json(name = "thumbnail") val thumbnail: String? = null
)