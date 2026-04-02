package com.rvalero.ecogrow.data.remote.utils

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
            level = LogLevel.BODY
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
                    val currentRefresh = tokenManager.getRefreshToken().firstOrNull() ?: return@refreshTokens null
                    val response: AuthResponseDto = client.post("$BASE_URL/auth/refresh") {
                        contentType(ContentType.Application.Json)
                        setBody(RefreshTokenRequestDto(refreshToken = currentRefresh))
                        markAsRefreshTokenRequest()
                    }.body()
                    tokenManager.saveTokens(response.accessToken, response.refreshToken)
                    BearerTokens(response.accessToken, response.refreshToken)
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
