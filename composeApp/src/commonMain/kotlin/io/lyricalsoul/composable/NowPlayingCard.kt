package io.lyricalsoul.composable

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.lyricalsoul.radio.RadioManagerStations
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.radio.models.payloads.SongInfo
import io.lyricalsoul.ui.Text
import io.lyricalsoul.ui.backgroundColor
import io.lyricalsoul.ui.h3TextStyle
import io.lyricalsoul.ui.h4TextStyle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme

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
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        println("NowPlayingCard: $song")
        Box(
            Modifier.clip(RoundedCornerShape(8.dp)).background(backgroundColor())
        ) {
            AsyncImage(
                model = song.art,
                placeholder = painterResource(currentStation.smallLogoDrawable),
                contentDescription = song.text,
                modifier = Modifier.width(200.dp),
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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