package io.lyricalsoul

import io.lyricalsoul.integrations.discord.PresenceClient

interface Platform {
    val name: String
    val rpcClient: PresenceClient
    val isDesktop: Boolean
}

expect fun getPlatform(): Platform