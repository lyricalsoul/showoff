package io.lyricalsoul

import io.lyricalsoul.integrations.discord.PresenceClient

interface AudioController {
    suspend fun play(url: String?)
    fun stop()
    fun isCurrentlyPlaying(): Boolean
}

expect fun getAudioController(): AudioController