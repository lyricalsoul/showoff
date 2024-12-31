package io.lyricalsoul

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.lyricalsoul.composable.*
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.events.AzuraConnectedEvent
import io.lyricalsoul.radio.models.events.AzuraListenerCountChangedEvent
import io.lyricalsoul.radio.models.events.AzuraSongChangedEvent
import io.lyricalsoul.radio.models.payloads.SongInfo
import io.lyricalsoul.ui.*
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
        var nowPlayingSong by remember { mutableStateOf<SongInfo?>(null) }
        var currentStation by remember { mutableStateOf<RadioStation?>(null) }
        var listenerInfo by remember { mutableStateOf<AzuraListenerCountChangedEvent?>(null) }
        var latestPlayedTracks by remember { mutableStateOf<List<SongInfo>>(emptyList()) }

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
                        showoff.attemptPresenceUpdate(currentStation!!, it.nowPlaying)
                        showoff.playRadioFromStream(it.nowPlaying)
                        latestPlayedTracks = it.nowPlaying.songHistory.map { it.song }
                        nowPlayingSong = it.nowPlaying.nowPlaying.song
                    }

                    is AzuraListenerCountChangedEvent -> listenerInfo = it
                    is AzuraConnectedEvent -> currentStation = it.station
                }
            }
        }

        Column(
            Modifier.fillMaxSize().background(backgroundColor()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // connected/is connecting text
            if (currentStation == null) {
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
                StationBar(currentStation!!)
            }

            // centralize the now playing card
            // make this a row: left side is the now playing card, right side is the song history
            Row(
                Modifier.fillMaxWidth().fillMaxHeight(0.85f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(0.55f)
                        .fillMaxHeight()
                        .background(texturedBackgroundBrush())
                        .clip(RoundedCornerShape(8.dp))
                        .border(Dp.Hairline, darkenColor(getTitleBarColor(), 0.01f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    nowPlayingSong?.let {
                        Box(Modifier.fillMaxSize().blur(8.dp))
                        NowPlayingCard(it, currentStation!!)
                    }
                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (latestPlayedTracks.isNotEmpty()) {
                        PreviouslyPlayedList(latestPlayedTracks)
                    }
                }
            }

            Box(
                Modifier.fillMaxHeight()
            ) {
                nowPlayingSong?.let {
                    AudioControlBar(
                        it,
                        currentStation!!
                    )
                } ?: AudioControlBarSkeleton()
            }
        }
    }
}

