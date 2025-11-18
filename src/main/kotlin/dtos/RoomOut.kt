package com.xavierclavel.dtos

import com.xavierclavel.enums.RoomStatus
import kotlinx.serialization.Serializable

/**
 * Represents a user in the system.
 *
 * @property username Display name of the user.
 */
@Serializable
data class RoomOut(
    val id: Long,
    val name: String,
    val capacity: Int,
    val status: RoomStatus,
)