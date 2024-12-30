package io.lyricalsoul

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.lyricalsoul.composable.TitleBarView
import io.lyricalsoul.ui.*
import io.lyricalsoul.radio.RadioManager
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.events.AzuraConnectedEvent
import io.lyricalsoul.radio.models.events.AzuraListenerCountChangedEvent
import io.lyricalsoul.radio.models.events.AzuraSongChangedEvent
import io.lyricalsoul.radio.models.events.AzuraSubscribedEvent
import io.lyricalsoul.radio.models.payloads.SongInfo
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import io.lyricalsoul.radio.RadioManagerStations
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.*
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.styling.DecoratedWindowStyle
import showoff.composeapp.generated.resources.Res
import showoff.composeapp.generated.resources.compose_multiplatform
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
                        nowPlayingSong = it.nowPlaying.nowPlaying.song
                    }

                    is AzuraListenerCountChangedEvent -> listenerInfo = it
                    is AzuraConnectedEvent -> currentStation = it.station
                }
            }
        }

        Box(
            Modifier.fillMaxSize().background(backgroundColor()),
        ) {
            Column(
                Modifier.fillMaxWidth().background(backgroundColor()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // connected/is connecting text
                if (currentStation == null) {
                    // bar skeleton
                    Text("Connecting to station...")
                } else {
                    StationBar(currentStation!!)
                }

                // centralize the now playing card
                Box(
                    Modifier.fillMaxWidth().padding(16.dp).background(backgroundColor()),
                    contentAlignment = Alignment.Center,
                ) {
                    nowPlayingSong?.let {
                        NowPlayingCard(it, currentStation!!)
                    }
                }
            }
        }
    }
}

// preview for now playing card
@Preview
@Composable
fun NowPlayingCardPreview() {
    IntUiTheme(isDark = true) {
        NowPlayingCard(
            SongInfo(
                title = "Song Title",
                artist = "Artist Name",
                art = "https://via.placeholder.com/150",
                text = "Song Title by Artist Name",
                id = "",
                album = "",
                genre = "TODO()",
                isrc = "TODO()",
                lyrics = "ODO()"
            ),
            RadioManagerStations.currentStation
        )
    }
}

// a card that shows what's playing right now. 2 rows: album image, the other is a column with song title, artist, album
@Composable
fun NowPlayingCard(song: SongInfo, currentStation: RadioStation) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        println("NowPlayingCard: $song")
        Box(
            Modifier.clip(RoundedCornerShape(8.dp)).background(backgroundColor())
        ) {
            AsyncImage(
                model = song.art,
                placeholder = painterResource(currentStation.smallLogoDrawable),
                contentDescription = song.text,
                modifier = Modifier.fillMaxWidth(0.2f),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            Modifier.fillMaxWidth().background(backgroundColor()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // title in bold
            Text(song.title, style = h3TextStyle())
            // artist in italic
            Text(song.artist, style = h4TextStyle())
            // add spacing
            Spacer(modifier = Modifier.height(4.dp))
            // station name
            Text(currentStation.name)
        }
    }
}

@Preview()
@Composable
fun StationBarPreview() {
    IntUiTheme(isDark = true) {
        StationBar(RadioManagerStations.currentStation)
    }
}

// station bar: shows the station logo, name, and the description
@Composable
fun StationBar(station: RadioStation) {
    Row(
        Modifier.fillMaxWidth().background(backgroundColor()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = station.logoUrl,
            placeholder = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = station.name,
            modifier = Modifier.fillMaxWidth(0.1f),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            Modifier.fillMaxWidth().scale(0.9f).background(backgroundColor()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // title in bold
            Text(station.name, style = h2TextStyle())
            // description
            Text(station.description, style = h4TextStyle())
        }
    }
}