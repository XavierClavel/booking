package com.xavierclavel.dtos

import com.xavierclavel.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * Represents a user in the system.
 *
 * @property username Display name of the user.
 */
@Serializable
data class BookingIn(
    @Serializable(with = LocalDateTimeSerializer::class)
    val startTime: LocalDateTime,

    @Serializable(with = LocalDateTimeSerializer::class)
    val endTime: LocalDateTime,

    val roomId: Long,
)