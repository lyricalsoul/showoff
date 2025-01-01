package io.lyricalsoul

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.dp
import io.lyricalsoul.components.desktop.TitleBarView
import io.lyricalsoul.radio.models.events.AzuraConnectedEvent
import io.lyricalsoul.radio.models.events.AzuraListenerCountChangedEvent
import io.lyricalsoul.radio.models.events.AzuraSongChangedEvent
import io.lyricalsoul.ui.Text
import io.lyricalsoul.ui.backgroundColor
import io.lyricalsoul.ui.h2TextStyle
import io.lyricalsoul.ui.h4TextStyle
import io.lyricalsoul.viewmodel.MainViewModel
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.dark
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.styling.DecoratedWindowStyle
import showoff.composeapp.generated.resources.Res
import showoff.composeapp.generated.resources.wbor
import kotlin.system.exitProcess

@Composable
fun ShowoffTheme(isDesktop: Boolean, content: @Composable () -> Unit) {
    if (isDesktop) {
        IntUiTheme(
            theme = JewelTheme.darkThemeDefinition(),
            styling = ComponentStyling.dark(),
            swingCompatMode = true
        ) {
            DecoratedWindow(
                onCloseRequest = { runBlocking { exitProcess(0) } },
                title = "Showoff Radio",
                style = DecoratedWindowStyle.dark(),
                icon = painterResource(Res.drawable.wbor),
                onKeyEvent = { event ->
                    processKeyShortcuts(event)
                },
                content = {
                    TitleBarView()
                    content()
                },
            )
        }
    } else {
        MaterialTheme(
            colors = darkColors(),
            content = content
        )
    }
}

@Composable
fun App() {
    val showoff = Showoff()

    ShowoffTheme(true) {
        if (showoff.isRPCAvailable) {
            LaunchedEffect(Unit) {
                showoff.connectDiscordRPC()
            }
        }

        LaunchedEffect(Unit) {
            val radioManager = showoff.radioManager

            radioManager.connectToStation(radioManager.stationList.first()) {
                when (it) {
                    is AzuraSongChangedEvent -> {
                        showoff.attemptPresenceUpdate(MainViewModel.currentStation!!, it.nowPlaying)
                        showoff.playRadioFromStream(it.nowPlaying)
                        MainViewModel.latestPlayedTracks = it.nowPlaying.songHistory.map { it.song }
                        MainViewModel.nowPlayingSong = it.nowPlaying.nowPlaying.song
                    }

                    is AzuraListenerCountChangedEvent -> MainViewModel.listenerInfo = it
                    is AzuraConnectedEvent -> MainViewModel.currentStation = it.station
                }
            }
        }

        if (MainViewModel.currentStation == null) {
            Column(
                Modifier.fillMaxWidth().fillMaxHeight().background(backgroundColor()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Hang tight!", style = h2TextStyle())
                Spacer(modifier = Modifier.height(8.dp))
                Text("Connecting to station...", style = h4TextStyle())
            }
        } else {
            key(MainViewModel.currentView) {
                MainViewModel.current()
            }
        }
    }
}

private fun processKeyShortcuts(keyEvent: KeyEvent): Boolean {
    return when (keyEvent.key) {
        Key.MediaPlayPause -> {
            Showoff().audioManager.togglePause()
            true
        }

        else -> {
            println("Unhandled key event: ${keyEvent.key}")
            false
        }
    }
}