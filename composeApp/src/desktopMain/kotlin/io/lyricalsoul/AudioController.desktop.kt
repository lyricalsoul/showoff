package io.lyricalsoul

import javazoom.jl.player.jlp

class DesktopAudioController : AudioController {
    private var jlPlayer: jlp? = null
    private var currentUrl: String? = null

    override suspend fun play(url: String?) {
        if (url == null && jlPlayer != null) {
            jlPlayer!!.play()
            return
        }

        if (url == null || currentUrl == url) return

        stop()
        jlPlayer = null

        jlPlayer = jlp.createInstance(arrayOf("-url", url))
        if (jlPlayer != null) {
            currentUrl = url
            jlPlayer!!.play()
        }
    }

    override fun stop() {
        jlPlayer?.stop()
    }

    override fun isCurrentlyPlaying(): Boolean {
        return jlPlayer != null
    }
}

val audioController = DesktopAudioController()

actual fun getAudioController(): AudioController {
    return audioController
}