package io.lyricalsoul.radio.models.payloads

import kotlinx.serialization.Serializable

// {"disconnect":{"code":3502,"reason":"stale"}}

@Serializable
data class DisconnectionInfo(
    val code: Int,
    val reason: String
)

@Serializable
data class DisconnectionMessage(
    val disconnect: DisconnectionInfo
)