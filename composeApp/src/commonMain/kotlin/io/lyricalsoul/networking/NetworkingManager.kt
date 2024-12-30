package io.lyricalsoul.networking

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json

class NetworkingManager {
    // ktor client
    val client = HttpClient(CIO) {
        install(WebSockets) {
            // ping every 3 seconds
            pingIntervalMillis = 3000
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
}