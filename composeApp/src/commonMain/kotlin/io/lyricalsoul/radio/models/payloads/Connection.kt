package io.lyricalsoul.radio.models.payloads

import kotlinx.serialization.Serializable

@Serializable
data class SubscribedStation(
    val recoverable: Boolean,
    val epoch: String,
    val offset: Long,
    val positioned: Boolean
)

@Serializable
data class ConnectionInfo(
    val client: String,
    val version: String,
    val subs: Map<String, SubscribedStation>,
    val ping: Long,
    val session: String,
    val time: Long
)

@Serializable
data class ConnectMessage(
    val connect: ConnectionInfo
)