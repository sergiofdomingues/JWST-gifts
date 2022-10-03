package com.example.jwst_gifts.data.network.response

import com.example.jwst_gifts.domain.model.SpaceProgram
import com.squareup.moshi.Json

data class SpaceProgramsResponse(
    @field:Json(name = "body") val data: List<SpaceProgramResponse>
) {
    fun toModel(): List<SpaceProgram> = data.mapNotNull { it.toModel() }
}