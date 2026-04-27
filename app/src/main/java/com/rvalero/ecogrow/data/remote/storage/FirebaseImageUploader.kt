package com.rvalero.ecogrow.data.remote.storage

import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.rvalero.ecogrow.common.NetworkResult
import com.rvalero.ecogrow.common.safeApiCall
import com.rvalero.ecogrow.domain.repository.ImageUploader
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseImageUploader(
    private val storage: FirebaseStorage
) : ImageUploader {

    override suspend fun upload(uri: String): NetworkResult<String> = safeApiCall {
        val ref = storage.reference.child("products/${UUID.randomUUID()}.jpg")
        ref.putFile(uri.toUri()).await()
        ref.downloadUrl.await().toString()
    }
}
