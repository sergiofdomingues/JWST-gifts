package com.example.jwst_gifts.domain.model

import com.example.jwst_gifts.data.network.response.DetailsResponse

data class SpaceProgram(
    val id: String,
    val imageUrl: String,
    val details: DetailsResponse? = null,
    val fileType: String? = null,
    val observationId: String? = null,
    val program: Int? = null,
    val thumbnail: String ? = null
)