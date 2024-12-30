package io.lyricalsoul.radio

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.events.*
import io.lyricalsoul.radio.models.payloads.SongInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URI

class RadioManager(private val httpClient: HttpClient) {
    private val shouldDisconnect = false
    private var isConnected = false

    val stationList = RadioManagerStations.stations

    private fun buildAzuraConnectionRequest(station: RadioStation): String {
        // {"subs":{"STATIONID":{}}}

        val txt = """
            {"subs":{"${station.stationId}":{}}}
        """.trimIndent()

        println("Sending connection request: $txt")
        return txt
    }

    // takes the full station url (wss://azura.wbor.org/api/live/nowplaying/websocket) and returns the host & path separately
    private fun parseUrl(url: String): Pair<String, String> {
        val uri = URI(url)
        return Pair(uri.host, uri.path)
    }

    // we connect to station and use a DSL to handle incoming messages
    fun connectToStation(station: RadioStation, handler: suspend RadioManager.(AzuracastEvent) -> Unit) {
        if (isConnected) {
            return
        }

        // connect to the websocket
        // when a message is received, call the handler
        isConnected = true

        val (host, path) = parseUrl(station.azuracastWebsocketUrl)
        println("Connecting to $host at $path")

        CoroutineScope(Dispatchers.Default).launch {
            try {
                httpClient.webSocket(
                    method = HttpMethod.Get,
                    host = host,
                    path = path
                ) {
                    // send the connection request
                    send(Frame.Text(buildAzuraConnectionRequest(station)))

                    // listen for messages
                    var currentSong: SongInfo? = null
                    var currentListenerCount: Int = 0

                    while (!shouldDisconnect) {
                        val frame = incoming.receive() as? Frame.Text ?: continue
                        val event = AzuracastEvent.fromJson(frame.readText()) ?: continue
                        println("Received event: $event")
                        if (event.type == AzuraEventType.SONG_CHANGED) {
                            val songChangedEvent = event.cast<AzuraSongChangedEvent>()

                            val npSong = songChangedEvent.nowPlaying.nowPlaying.song
                            val currentListeners = songChangedEvent.nowPlaying.listeners.current

                            if (currentSong != npSong) {
                                currentSong = npSong
                                handler(songChangedEvent)
                            }

                            if (currentListeners != currentListenerCount) {
                                currentListenerCount = currentListeners
                                val dispatchedEvent = AzuraListenerCountChangedEvent(
                                    currentListeners = currentListeners,
                                    totalListeners = songChangedEvent.nowPlaying.listeners.total,
                                    uniqueListeners = songChangedEvent.nowPlaying.listeners.unique
                                )

                                handler(dispatchedEvent)
                            }
                        } else if (event.type == AzuraEventType.SUBSCRIBED) {
                            val connectedEvent = AzuraConnectedEvent(station)
                            send(Frame.Ping(byteArrayOf()))
                            handler(connectedEvent)
                        } else {
                            handler(event)
                        }
                    }

                    delay(1000)
                }
            } catch (e: Exception) {
                // reconnect
                println("Failed to connect to station: $e")
                isConnected = false
                // re-run the function and kill the current coroutine
                connectToStation(station, handler)
                return@launch
            }
        }
    }
}