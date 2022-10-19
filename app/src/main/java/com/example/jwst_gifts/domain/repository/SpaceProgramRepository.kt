package com.example.jwst_gifts.domain.repository

import com.example.jwst_gifts.domain.util.Failure
import com.example.jwst_gifts.domain.model.SpaceProgram
import com.example.jwst_gifts.domain.util.Either
import kotlinx.coroutines.flow.Flow

interface SpaceProgramRepository {
    fun observeSpacePrograms(): Flow<List<SpaceProgram>>
    suspend fun loadMoreSpacePrograms(): Either<Failure, Unit>
}