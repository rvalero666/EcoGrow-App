package com.rvalero.ecogrow.domain.repository

import com.rvalero.ecogrow.common.NetworkResult

interface ImageUploader {
    suspend fun upload(uri: String): NetworkResult<String>
}
