package com.rvalero.ecogrow.common

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlin.coroutines.cancellation.CancellationException

sealed class NetworkResult<out T> {

    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int?, val message: String?) : NetworkResult<Nothing>()

    fun <R> then(
        onSuccess: (T) -> R,
        onError: (Int?, String?) -> R
    ): R = when (this) {
        is Success -> onSuccess(data)
        is Error -> onError(code, message)
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall())
    } catch (e: ClientRequestException) {
        NetworkResult.Error(e.response.status.value, e.message)
    } catch (e: ServerResponseException) {
        NetworkResult.Error(e.response.status.value, e.message)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        NetworkResult.Error(null, e.message)
    }
}
