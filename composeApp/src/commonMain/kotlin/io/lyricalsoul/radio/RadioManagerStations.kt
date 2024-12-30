package io.lyricalsoul.radio

import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.RadioStationType
import org.jetbrains.jewel.ui.icon.PathIconKey
import showoff.composeapp.generated.resources.Res
import showoff.composeapp.generated.resources.wbor

object RadioManagerStations {
    val stations = listOf(
        RadioStation(
            name = "WBOR 91.1 FM",
            location = "Brunswick, ME, USA",
            homepage = "https://wbor.org/",
            frequency = "91.1",
            type = RadioStationType.FM,
            logoUrl = "https://wbor.org/assets/images/logo.png",
            smallLogoUrl = "https://wbor.org/assets/images/favicon.png",
            smallLogoDrawable = Res.drawable.wbor,
            description = "24/7 non-commercial radio from Bowdoin College, Brunswick, Maine, since 1941.",
            azuracastWebsocketUrl = "wss://azura.wbor.org/api/live/nowplaying/websocket",
            stationId = "station:wbor"
        ),
    )

    // get/set
    var currentStation: RadioStation = stations.first()
}