package com.example.jwst_gifts.data.remote.service

import com.example.jwst_gifts.data.remote.response.SpaceProgramsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JWSTService {

    @GET("all/type/jpg")
    suspend fun getAllByFileType(
        @Query("page") page: String,
        @Query("perPage") perPage: String = "20"
    ): Response<SpaceProgramsResponse>
}