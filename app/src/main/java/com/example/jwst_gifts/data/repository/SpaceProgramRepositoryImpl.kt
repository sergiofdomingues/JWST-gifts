package com.example.jwst_gifts.data.repository

import com.example.jwst_gifts.domain.util.Failure
import com.example.jwst_gifts.domain.util.Failure.RequestInProgress
import com.example.jwst_gifts.data.mappers.toListOfSpacePrograms
import com.example.jwst_gifts.data.remote.service.JWSTService
import com.example.jwst_gifts.domain.model.SpaceProgram
import com.example.jwst_gifts.data.remote.response.SpaceProgramsResponse
import com.example.jwst_gifts.domain.repository.SpaceProgramRepository
import com.example.jwst_gifts.domain.util.Either
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SpaceProgramRepositoryImpl @Inject constructor(private val spaceProgramService: JWSTService) :
    BaseRepository(), SpaceProgramRepository {

    private var loadState: LoadState = LoadState.Initial
    private var isRequestInProgress = false
    private var lastPageRequested = STARTING_PAGE_INDEX

    private val _spaceProgramResults = MutableStateFlow<List<SpaceProgram>>(emptyList())

    override fun observeSpacePrograms(): StateFlow<List<SpaceProgram>> = _spaceProgramResults

    override suspend fun loadMoreSpacePrograms(): Either<Failure, Unit> {
        if (isRequestInProgress) return Either.Failure(RequestInProgress)

        loadState.also { state ->
            if (state !is LoadState.Done) {
                isRequestInProgress = true

                return when (val response =
                    requestLaunches(page = if (state is LoadState.HasNext) state.nextPage else STARTING_PAGE_INDEX)) {
                    is Either.Success -> {
                        val data = response.data.toListOfSpacePrograms()
                        if (data.size < PAGE_SIZE) {
                            loadState = LoadState.Done
                        } else {
                            lastPageRequested += data.size
                            setNextPageState(nextPage = lastPageRequested + 1)
                            _spaceProgramResults.emit(value = _spaceProgramResults.value + data)
                        }
                        isRequestInProgress = false
                        Either.Success(Unit)
                    }
                    is Either.Failure -> {
                        isRequestInProgress = false
                        response
                    }
                }
            } else return Either.Success(Unit)
        }
    }


    private fun setNextPageState(nextPage: Int) {
        loadState = LoadState.HasNext(nextPage = nextPage)
    }

    private suspend fun requestLaunches(
        page: Int,
        perPage: Int = PAGE_SIZE
    ): Either<Failure, SpaceProgramsResponse> =
        safeApiCall {
            spaceProgramService.getAllByFileType(page = page.toString(), perPage = perPage.toString())
        }

    sealed class LoadState {
        object Initial : LoadState()
        data class HasNext(val nextPage: Int) : LoadState()
        object Done : LoadState()
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        private const val PAGE_SIZE = 20
    }
}