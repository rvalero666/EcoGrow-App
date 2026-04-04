package com.rvalero.ecogrow.data.remote.utils

import com.rvalero.ecogrow.BuildConfig
import com.rvalero.ecogrow.common.CrashlyticsLogger
import com.rvalero.ecogrow.data.model.ApiResponseDto
import com.rvalero.ecogrow.data.model.auth.AuthResponseDto
import com.rvalero.ecogrow.data.model.auth.RefreshTokenRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

const val BASE_URL = "http://10.0.2.2:8080"

fun provideHttpClient(tokenManager: TokenManager): HttpClient {
    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = false
            })
        }

        install(Logging) {
            level = if (BuildConfig.DEBUG) LogLevel.BODY else LogLevel.NONE
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val access = tokenManager.getAccessToken().firstOrNull()
                    val refresh = tokenManager.getRefreshToken().firstOrNull()
                    if (access != null && refresh != null) {
                        BearerTokens(access, refresh)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    try {
                        val currentRefresh = tokenManager.getRefreshToken().firstOrNull()
                            ?: return@refreshTokens null
                        val response: ApiResponseDto<AuthResponseDto> = client.post("$BASE_URL/auth/refresh") {
                            contentType(ContentType.Application.Json)
                            setBody(RefreshTokenRequestDto(refreshToken = currentRefresh))
                            markAsRefreshTokenRequest()
                        }.body()
                        if (!response.success || response.data == null) {
                            tokenManager.clearTokens()
                            return@refreshTokens null
                        }
                        tokenManager.saveTokens(response.data.accessToken, response.data.refreshToken)
                        BearerTokens(response.data.accessToken, response.data.refreshToken)
                    } catch (e: Exception) {
                        CrashlyticsLogger.logException(e)
                        tokenManager.clearTokens()
                        null
                    }
                }

                sendWithoutRequest { request ->
                    !request.url.encodedPath.contains("/auth/")
                }
            }
        }

        defaultRequest {
            url(BASE_URL)
            contentType(ContentType.Application.Json)
        }
    }
}
