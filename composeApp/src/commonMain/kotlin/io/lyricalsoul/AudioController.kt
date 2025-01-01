package io.lyricalsoul

import androidx.compose.runtime.MutableState

interface AudioController {
    var isCurrentlyPlaying: MutableState<Boolean>
    suspend fun play(url: String)
    fun stop()
    fun pause()
    fun togglePause()
    fun resume()
}

expect fun getAudioController(): AudioController