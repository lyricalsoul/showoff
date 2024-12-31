package io.lyricalsoul.audio

import javazoom.jl.decoder.Bitstream
import javazoom.jl.decoder.Decoder
import javazoom.jl.decoder.SampleBuffer
import javazoom.jl.player.AudioDevice
import javazoom.jl.player.FactoryRegistry
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL

class ShowoffJVMAudioPlayer(
    private var stream: InputStream,
    private val url: String,
    device: AudioDevice? = null
) {
    private val bitstream = Bitstream(stream)
    private val decoder = Decoder()
    private var audio: AudioDevice? = null
    private var closed = false
    private var complete = false
    private var paused = false

    val isComplete: Boolean
        @Synchronized
        get() = complete

    val isPlaying: Boolean
        get() = audio != null && !paused

    init {
        audio = device ?: FactoryRegistry.systemRegistry().createAudioDevice().also {
            it.open(decoder)
        }
    }

    constructor(url: String, device: AudioDevice? = null) : this(getInputStream(url), url, device)

    @Synchronized
    fun stop() {
        synchronized(this) {
            complete = true
            closed = true
            audio?.close()
            audio = null
            runCatching { bitstream.close() }
        }
    }

    @Synchronized
    fun pause() {
        synchronized(this) {
            paused = true
        }
    }

    @Synchronized
    fun resume() {
        synchronized(this) {
            paused = false
        }
    }

    fun play() = playStream()

    private fun playStream(): Boolean {
        var remainingFrames = Int.MAX_VALUE
        var result = true

        while (remainingFrames-- > 0 && result) {
            result = decodeFrame()
        }

        if (!result) {
            audio?.run {
                flush()
                synchronized(this@ShowoffJVMAudioPlayer) {
                    complete = !closed
                    close()
                }
            }
        }

        return result
    }

    private fun decodeFrame(): Boolean {
        return try {
            val header = bitstream.readFrame() ?: return false

            (decoder.decodeFrame(header, bitstream) as SampleBuffer).run {
                synchronized(this@ShowoffJVMAudioPlayer) {
                    if (paused) {
                        // Skip the frame
                        bitstream.closeFrame()
                        return true
                    }

                    audio?.write(buffer, 0, bufferLength)
                }
            }

            bitstream.closeFrame()
            true
        } catch (ex: RuntimeException) {
            // if it was Stream closed, reopen it and begin playing again
            if (ex.message?.contains("Stream closed") == true) {
                Thread.sleep(3000)
                restart()
                return true
            }
            throw RuntimeException("Failed to decode frame", ex)
        }
    }

    private fun restart() {
        stop()
        stream = getInputStream(url)
        playStream()
    }

    companion object {
        fun getInputStream(url: String): InputStream {
            val stream = URL(url).openStream()
            return BufferedInputStream(stream)
        }
    }
}