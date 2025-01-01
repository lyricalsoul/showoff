package io.lyricalsoul.viewmodel

import androidx.compose.runtime.*
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.events.AzuraListenerCountChangedEvent
import io.lyricalsoul.radio.models.payloads.SongInfo
import io.lyricalsoul.view.PlayerView
import io.lyricalsoul.view.SettingsView


object MainViewModel {
    var nowPlayingSong by mutableStateOf<SongInfo?>(null)
    var currentStation by mutableStateOf<RadioStation?>(null)
    var listenerInfo by mutableStateOf<AzuraListenerCountChangedEvent?>(null)
    var latestPlayedTracks by mutableStateOf<List<SongInfo>>(emptyList())

    fun navigateTo(title: String) {
        val view = availableViews.find { it.title == title }
        if (view != null) {
            currentView = view
        }
    }

    var currentView by mutableStateOf(availableViews.first())

    @Composable
    fun current() = currentView.content()

    fun atHome() = currentView.title == "Home"
}

private val availableViews = mutableStateListOf(
    AppView(title = "Home", content = { PlayerView() }),
    AppView(title = "Settings", content = { SettingsView() }),
)