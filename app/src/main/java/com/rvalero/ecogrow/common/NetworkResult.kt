package com.rvalero.ecogrow.common

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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

private suspend fun extractErrorMessage(response: HttpResponse): String {
    return try {
        val text = response.bodyAsText()
        val json = Json.parseToJsonElement(text).jsonObject
        json["message"]?.jsonPrimitive?.content ?: text
    } catch (_: Exception) {
        "Error ${response.status.value}"
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(apiCall())
    } catch (e: ClientRequestException) {
        val message = extractErrorMessage(e.response)
        NetworkResult.Error(e.response.status.value, message)
    } catch (e: ServerResponseException) {
        CrashlyticsLogger.logException(e)
        val message = extractErrorMessage(e.response)
        NetworkResult.Error(e.response.status.value, message)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        CrashlyticsLogger.logException(e)
        NetworkResult.Error(null, e.message)
    }
}
