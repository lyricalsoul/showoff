package io.lyricalsoul

import io.lyricalsoul.networking.NetworkingManager
import io.lyricalsoul.radio.RadioManager
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.payloads.NowPlayingInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val mainNetworkManager = NetworkingManager()
val mainRadioManager = RadioManager(mainNetworkManager.client)

class Showoff {
    private val platform = getPlatform()
    private val networkManager = mainNetworkManager
    var audioManager = getAudioController()
    val radioManager = mainRadioManager

    val isRPCAvailable: Boolean
        get() = platform.rpcClient.isAvailable

    val isDesktop: Boolean
        get() = platform.isDesktop

    suspend fun connectDiscordRPC() {
        if (!platform.rpcClient.isAvailable) return

        platform.rpcClient.connect()
    }

    fun attemptPresenceUpdate(station: RadioStation, np: NowPlayingInfo) {
        if (!platform.rpcClient.isAvailable) return

        // launch coroutine to update presence
        CoroutineScope(Dispatchers.Default).launch {
            platform.rpcClient.updatePresence(np, station)
        }
    }

    fun playRadioFromStream(np: NowPlayingInfo) {
        CoroutineScope(Dispatchers.Default).launch {
            audioManager.play(np.station.mounts.first().url)
        }
    }
}