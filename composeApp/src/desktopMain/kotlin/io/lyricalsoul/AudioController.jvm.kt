package io.lyricalsoul

import io.lyricalsoul.audio.ShowoffJVMAudioPlayer

class DesktopAudioController : AudioController {
    private var player: ShowoffJVMAudioPlayer? = null

    override suspend fun play(url: String) {
        stop()
        player = ShowoffJVMAudioPlayer(url, null)
        player?.play()
    }

    override fun resume() {
        player?.resume()
    }

    override fun pause() {
        player?.pause()
    }

    override fun stop() {
        player?.stop()
    }

    override fun isCurrentlyPlaying() = player?.isPlaying ?: false
}

val audioController = DesktopAudioController()

actual fun getAudioController(): AudioController {
    return audioController
}