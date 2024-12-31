package io.lyricalsoul

interface AudioController {
    suspend fun play(url: String)
    fun stop()
    fun pause()
    fun isCurrentlyPlaying(): Boolean
    fun resume()
}

expect fun getAudioController(): AudioController