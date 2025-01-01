package io.lyricalsoul

import androidx.compose.runtime.mutableStateOf
import io.lyricalsoul.audio.ShowoffJVMAudioPlayer

class DesktopAudioController : AudioController {
    private var player: ShowoffJVMAudioPlayer? = null
    override var isCurrentlyPlaying = mutableStateOf(true)

    override suspend fun play(url: String) {
        stop()
        player = ShowoffJVMAudioPlayer(url, null)
        player?.play()
    }

    override fun resume() {
        isCurrentlyPlaying.value = true
        player?.resume()
    }

    override fun pause() {
        isCurrentlyPlaying.value = false
        player?.pause()
    }

    override fun stop() {
        isCurrentlyPlaying.value = false
        player?.stop()
    }

    override fun togglePause() {
        if (isCurrentlyPlaying.value) {
            pause()
        } else {
            resume()
        }
    }
}

val audioController = DesktopAudioController()

actual fun getAudioController(): AudioController {
    return audioController
}