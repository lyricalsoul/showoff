package io.lyricalsoul.rpc

import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.data.activity.Activity
import io.lyricalsoul.integrations.discord.PresenceClient
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.payloads.NowPlayingInfo

class DesktopPresenceClient : PresenceClient() {
    private val showoffClientId = "1322777308800745494"
    private val ipc: KDiscordIPC = KDiscordIPC(showoffClientId)

    override val isAvailable: Boolean = true

    override suspend fun connect() {
        ipc.on<ReadyEvent> {
            println("Ready! (${data.user.username}#${data.user.discriminator})")
        }

        ipc.connect()
    }

    override suspend fun updatePresence(
        np: NowPlayingInfo,
        station: RadioStation
    ) {
        val song = np.nowPlaying.song
        val songName = song.title
        val artistName = song.artist
        val stationName = station.name
        val stationLocation = station.location
        val albumCoverUrl = song.art ?: ""
        val stationLogoUrl = station.smallLogoUrl
        val radioUrl = station.homepage

        val currentListeners = np.listeners.current

        ipc.activityManager.setActivity(
            Activity(
                details = "🎶 $songName by $artistName",
                state = "📻 $stationName - $stationLocation",
                assets = Activity.Assets(
                    largeImage = albumCoverUrl,
                    largeText = "$songName by $artistName",
                    smallImage = stationLogoUrl,
                    smallText = "$stationName - $currentListeners listeners"
                ),
                timestamps = Activity.Timestamps(
                    start = System.currentTimeMillis() - np.nowPlaying.elapsed * 1000,
                    end = null
                ),
                buttons = mutableListOf(
                    Activity.Button(
                        label = "🌎 Listen to $stationName",
                        url = radioUrl
                    )
                )
            )
        )
    }
}