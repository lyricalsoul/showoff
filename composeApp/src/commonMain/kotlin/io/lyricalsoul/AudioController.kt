package io.lyricalsoul

interface AudioController {
    suspend fun play(url: String?)
    fun stop()
    fun isCurrentlyPlaying(): Boolean
}

expect fun getAudioController(): AudioController