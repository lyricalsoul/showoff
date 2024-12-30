package io.lyricalsoul.radio.models.events

import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.payloads.ConnectMessage
import io.lyricalsoul.radio.models.payloads.DisconnectionMessage
import io.lyricalsoul.radio.models.payloads.NowPlayingInfo
import io.lyricalsoul.radio.models.payloads.NowPlayingMessage
import kotlinx.serialization.json.Json

val decoder = Json {
    ignoreUnknownKeys = true
}

abstract class AzuracastEvent {
    abstract val type: AzuraEventType

    fun <T : AzuracastEvent> cast(): T {
        return this as T
    }

    companion object {
        fun fromJson(json: String): AzuracastEvent? {
            if (json.startsWith("{\"connect\":")) {
                // we can parse this as a ConnectMessage
                val data = decoder.decodeFromString<ConnectMessage>(json)
                val stationId = data.connect.subs.keys.first()
                return AzuraSubscribedEvent(stationId)
            } else if (json.startsWith("{\"disconnect\":")) {
                // we can parse this as a DisconnectionMessage
                val data = decoder.decodeFromString<DisconnectionMessage>(json)
                return AzuraDisconnectedEvent(data.disconnect.code, data.disconnect.reason)
            } else if (json == "{}") {
                return null
            } else {
                try {
                    val data = decoder.decodeFromString<NowPlayingMessage>(json)
                    return AzuraSongChangedEvent(data.publication.data.np)
                } catch (e: Exception) {
                    println("failed to decode json: $json\n\nexception: $e")
                    return null
                }
            }
        }
    }
}

data class AzuraSubscribedEvent(
    val stationId: String
) : AzuracastEvent() {
    override val type = AzuraEventType.SUBSCRIBED
}

data class AzuraConnectedEvent(
    val station: RadioStation
) : AzuracastEvent() {
    override val type = AzuraEventType.CONNECTED
}

data class AzuraSongChangedEvent(
    val nowPlaying: NowPlayingInfo
) : AzuracastEvent() {
    override val type = AzuraEventType.SONG_CHANGED
}

data class AzuraListenerCountChangedEvent(
    val currentListeners: Int,
    val totalListeners: Int,
    val uniqueListeners: Int
) : AzuracastEvent() {
    override val type = AzuraEventType.LISTENER_COUNT_CHANGED
}

data class AzuraDisconnectedEvent(
    val code: Int,
    val reason: String
) : AzuracastEvent() {
    override val type = AzuraEventType.DISCONNECTED
}