package io.lyricalsoul

import io.lyricalsoul.networking.NetworkingManager
import io.lyricalsoul.radio.RadioManager
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.events.AzuracastEvent
import io.lyricalsoul.radio.models.payloads.NowPlayingInfo
import io.lyricalsoul.radio.models.payloads.SongInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Showoff {
    private val platform = getPlatform()
    private val networkManager = NetworkingManager()
    var audioManager = getAudioController()
    val radioManager = RadioManager(networkManager.client)

    val isRPCAvailable: Boolean
        get() = platform.rpcClient.isAvailable

    val isDesktop: Boolean
        get() = platform.isDesktop

    suspend fun connectDiscordRPC () {
        if (!platform.rpcClient.isAvailable) return

        platform.rpcClient.connect()
    }

    fun attemptPresenceUpdate (station: RadioStation, np: NowPlayingInfo) {
        if (!platform.rpcClient.isAvailable) return

        // launch coroutine to update presence
        CoroutineScope(Dispatchers.Default).launch {
            platform.rpcClient.updatePresence(np, station)
        }
    }

    fun playRadioFromStream (np: NowPlayingInfo) {
        CoroutineScope(Dispatchers.Default).launch {
            audioManager.play(np.station.mounts.first().url)
        }
    }
}