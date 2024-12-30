package io.lyricalsoul.integrations.discord

import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.payloads.NowPlayingInfo

abstract class PresenceClient {
    open val isAvailable: Boolean = false

    open suspend fun connect() {
        error("Discord IPC is not available on this platform.")
    }

    open suspend fun updatePresence (
        np: NowPlayingInfo,
        station: RadioStation
    ) {
        error("Discord IPC is not available on this platform.")
    }
}