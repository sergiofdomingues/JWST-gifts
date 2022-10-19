package com.example.jwst_gifts.data.mappers

import com.example.jwst_gifts.data.remote.response.SpaceProgramResponse
import com.example.jwst_gifts.data.remote.response.SpaceProgramsResponse
import com.example.jwst_gifts.domain.model.SpaceProgram

fun SpaceProgramsResponse.toListOfSpacePrograms(): List<SpaceProgram> =
    data.mapNotNull { it.toSpaceProgram() }

fun SpaceProgramResponse.toSpaceProgram(): SpaceProgram? {
    return if (id == null || location == null) null
    else SpaceProgram(
        id = id,
        details = details,
        fileType = fileType,
        imageUrl = location,
        observationId = observationId,
        program = program,
        thumbnail = thumbnail
    )
}