package io.lyricalsoul.composable

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil3.compose.AsyncImage
import io.lyricalsoul.radio.RadioManagerStations
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.payloads.SongInfo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import showoff.composeapp.generated.resources.Res
import showoff.composeapp.generated.resources.compose_multiplatform
import androidx.compose.runtime.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.*
import io.lyricalsoul.Showoff
import io.lyricalsoul.ui.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.jewel.ui.component.copyWithSize

// preview for now playing card
@Preview()
@Composable
fun AudioControlBarPreview() {
    IntUiTheme(isDark = true) {
        AudioControlBar(
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

// a box that fills the width with the screen and contains the song cover, the name of the song, the artist, and the station name. also the play/pause button
// should also be at the bottom of the screen
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun AudioControlBar(song: SongInfo, currentStation: RadioStation) {
    var isPaused by remember { mutableStateOf(false) }

    Box(
        Modifier.fillMaxWidth().fillMaxHeight().background(translucentBlack()).padding(8.dp)
    ) {
        Box(Modifier.fillMaxSize().blur(4.dp))
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.clip(RoundedCornerShape(8.dp)).background(backgroundColor())
            ) {
                AsyncImage(
                    model = song.art,
                    placeholder = painterResource(currentStation.smallLogoDrawable),
                    contentDescription = song.text,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                // title in bold
                Text(song.title, style = h3TextStyle().copyWithSize(16.sp))
                // artist in italic
                Text(song.artist, style = h4TextStyle().copyWithSize(12.sp))
            }

            Spacer(modifier = Modifier.width(16.dp))
            // play/pause button
            Image(
                painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "Play/Pause",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = {
                        isPaused = !isPaused
                        if (isPaused) Showoff().audioManager.stop()
                        else {
                            GlobalScope.launch {
                                Showoff().audioManager.play(null)
                            }
                        }
                    })
                    .align(Alignment.Bottom),
                colorFilter = ColorFilter.tint(
                    androidx.compose.ui.graphics.Color.White
                )
            )
        }
    }
}

@Composable
fun AudioControlBarSkeleton() {
    Row(
        Modifier.fillMaxSize().background(translucentBlack()).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // title in bold
            Text("Obtaining current track...", style = h3TextStyle().copyWithSize(16.sp))
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}