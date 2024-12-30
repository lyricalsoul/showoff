package io.lyricalsoul.radio.models

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.jewel.ui.icon.PathIconKey

data class RadioStation(
    val name: String,
    val homepage: String,
    val location: String,
    val frequency: String,
    val type: RadioStationType,
    val logoUrl: String,
    val smallLogoUrl: String,
    val smallLogoDrawable: DrawableResource,
    val description: String,
    val azuracastWebsocketUrl: String,
    val stationId: String
)

