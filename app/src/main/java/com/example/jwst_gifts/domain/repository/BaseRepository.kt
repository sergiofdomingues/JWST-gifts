package com.example.jwst_gifts.domain.repository

import com.example.jwst_gifts.data.Either
import com.example.jwst_gifts.data.Failure
import com.example.jwst_gifts.data.Failure.NetworkConnection
import com.example.jwst_gifts.data.Failure.UnexpectedFailure
import com.example.jwst_gifts.data.network.response.ErrorResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): Either<Failure, T> {

        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()

                if (response.isSuccessful) {
                    Either.Success(data = response.body()!!)
                } else {
                    val errorResponse: ErrorResponse? = convertErrorBody(response.errorBody())
                    Either.Failure(failureType = Failure.ServerError(message = errorResponse?.errorMessage))
                }

            } catch (e: HttpException) {
                Either.Failure(failureType = UnexpectedFailure())
            } catch (e: IOException) {
                Either.Failure(failureType = NetworkConnection())
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                else Either.Failure(failureType = UnexpectedFailure())
            }
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorResponse? {
        return try {
            errorBody?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}