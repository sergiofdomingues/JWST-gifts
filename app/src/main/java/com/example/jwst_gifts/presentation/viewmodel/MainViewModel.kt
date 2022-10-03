package com.example.jwst_gifts.presentation.viewmodel

import androidx.lifecycle.*
import com.example.jwst_gifts.data.Either
import com.example.jwst_gifts.data.Failure
import com.example.jwst_gifts.domain.model.SpaceProgram
import com.example.jwst_gifts.data.repository.SpaceProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val VISIBLE_THRESHOLD = 5

@HiltViewModel
class MainViewModel @Inject constructor(
    private val spaceProgramRepository: SpaceProgramRepository
) : ViewModel() {

    private var job: Job? = null

    val loadMorePhotos: (LoadMore) -> Unit
    private val _spacePrograms = MutableLiveData<List<SpaceProgram>>()
    var spacePrograms: LiveData<List<SpaceProgram>> = _spacePrograms
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errors = MutableLiveData<String>()
    val errors: LiveData<String> = _errors

    private fun loadSpacePhotos() {
        job = viewModelScope.launch {
            _isLoading.value = true
            when (val response = spaceProgramRepository.loadMoreSpacePrograms()) {
                is Either.Success -> _isLoading.value = false
                is Either.Failure -> {
                    if (response.failureType !is Failure.RequestInProgress) {
                        response.failure?.errorMessage?.let { _errors.postValue(it) }
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    init {
        loadMorePhotos = { action -> if (action.shouldLoadMore()) loadSpacePhotos() }
        spacePrograms = spaceProgramRepository.observeSpacePrograms().asLiveData()
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}

data class LoadMore(
    val visibleItemCount: Int,
    val lastVisibleItemPosition: Int,
    val totalItemCount: Int
) {
    fun shouldLoadMore() =
        visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount
}