package com.example.jwst_gifts.data.repository

import com.example.jwst_gifts.data.Either
import com.example.jwst_gifts.data.Failure
import com.example.jwst_gifts.domain.model.SpaceProgram
import kotlinx.coroutines.flow.Flow

interface SpaceProgramRepository {
    fun observeSpacePrograms(): Flow<List<SpaceProgram>>
    suspend fun loadMoreSpacePrograms(): Either<Failure, Unit>
}