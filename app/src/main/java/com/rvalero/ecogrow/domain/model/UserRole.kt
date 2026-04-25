package com.rvalero.ecogrow.domain.model

enum class UserRole {
    CONSUMER,
    PRODUCER;

    companion object {
        fun fromBackendString(value: String?): UserRole = when (value?.lowercase()) {
            "productor" -> PRODUCER
            else -> CONSUMER
        }
    }

    fun toBackendString(): String = when (this) {
        CONSUMER -> "consumidor"
        PRODUCER -> "productor"
    }
}
