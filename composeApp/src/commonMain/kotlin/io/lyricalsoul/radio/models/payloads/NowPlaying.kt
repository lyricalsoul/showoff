package io.lyricalsoul.radio.models.payloads

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviouslyPlayedTrackInfo(
    @SerialName("sh_id") val shId: Int,
    @SerialName("played_at") val playedAt: Int,
    @SerialName("duration") val duration: Int,
    @SerialName("playlist") val playlist: String,
    @SerialName("streamer") val streamer: String,
    @SerialName("is_request") val isRequest: Boolean,
    @SerialName("song") val song: SongInfo
)

@Serializable
data class SongInfo(
    @SerialName("id") val id: String,
    @SerialName("art") val art: String?,
    @SerialName("text") val text: String,
    @SerialName("artist") val artist: String,
    @SerialName("title") val title: String,
    @SerialName("album") val album: String,
    @SerialName("genre") val genre: String,
    @SerialName("isrc") val isrc: String,
    @SerialName("lyrics") val lyrics: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongInfo

        if (id != other.id) return false
        if (artist != other.artist) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

@Serializable
data class NowPlayingTrackInfo(
    @SerialName("sh_id") val shId: Int,
    @SerialName("played_at") val playedAt: Int,
    @SerialName("duration") val duration: Int,
    @SerialName("playlist") val playlist: String,
    @SerialName("streamer") val streamer: String,
    @SerialName("is_request") val isRequest: Boolean,
    @SerialName("song") val song: SongInfo,
    @SerialName("elapsed") val elapsed: Int,
    @SerialName("remaining") val remaining: Int
)

@Serializable
data class Live(
    @SerialName("is_live") val isLive: Boolean,
    @SerialName("streamer_name") val streamerName: String
)

@Serializable
data class ListenerInfo(
    @SerialName("total") val total: Int,
    @SerialName("unique") val unique: Int,
    @SerialName("current") val current: Int
)

@Serializable
data class StreamMountInfo(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("bitrate") val bitrate: Int?,
    @SerialName("format") val format: String?,
    @SerialName("listeners") val listeners: ListenerInfo,
    @SerialName("path") val path: String,
    @SerialName("is_default") val isDefault: Boolean
)

@Serializable
data class StationInfo(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("shortcode") val shortcode: String,
    @SerialName("description") val description: String,
    @SerialName("frontend") val frontend: String,
    @SerialName("backend") val backend: String,
    @SerialName("timezone") val timezone: String,
    @SerialName("listen_url") val listenUrl: String,
    @SerialName("url") val url: String,
    @SerialName("public_player_url") val publicPlayerUrl: String,
    @SerialName("playlist_pls_url") val playlistPlsUrl: String,
    @SerialName("playlist_m3u_url") val playlistM3uUrl: String,
    @SerialName("is_public") val isPublic: Boolean,
    @SerialName("mounts") val mounts: List<StreamMountInfo>,
    @SerialName("hls_enabled") val hlsEnabled: Boolean,
    @SerialName("hls_is_default") val hlsIsDefault: Boolean,
    @SerialName("hls_url") val hlsUrl: String?,
    @SerialName("hls_listeners") val hlsListeners: Int,
    @SerialName("remotes") val remotes: List<String>
)

@Serializable
data class NowPlayingInfo(
    @SerialName("station") val station: StationInfo,
    @SerialName("listeners") val listeners: ListenerInfo,
    @SerialName("live") val live: Live,
    @SerialName("now_playing") val nowPlaying: NowPlayingTrackInfo,
    @SerialName("song_history") val songHistory: List<PreviouslyPlayedTrackInfo>,
    @SerialName("is_online") val isOnline: Boolean,
    @SerialName("cache") val cache: String
)

@Serializable
data class Data(
    @SerialName("np") val np: NowPlayingInfo,
    @SerialName("current_time") val currentTime: Int
)

@Serializable
data class Pub(
    @SerialName("data") val data: Data,
    @SerialName("offset") val offset: Int
)

@Serializable
data class NowPlayingMessage(
    @SerialName("channel") val channel: String,
    @SerialName("pub") val publication: Pub
)