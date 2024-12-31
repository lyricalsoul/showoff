package io.lyricalsoul.composable

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import io.lyricalsoul.radio.models.payloads.SongInfo
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import io.lyricalsoul.ui.*
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.ui.component.copyWithSize

@Preview
@Composable
fun PreviouslyPlayedListPreview() {
    val song = SongInfo(
        title = "Song Title",
        artist = "Artist Name",
        art = "https://coverartarchive.org/release/6682f0f1-426a-43e4-a003-a8146365e41b/17422813399-500.jpg",
        text = "Song Title by Artist Name",
        id = "",
        album = "",
        genre = "TODO()",
        isrc = "TODO()",
        lyrics = "ODO()"
    )

    // repeat 20 songs
    val songs = List(20) { song }

    IntUiTheme(isDark = true) {
        PreviouslyPlayedList(songs)
    }
}

@Composable
fun PreviouslyPlayedList(songs: List<SongInfo>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(backgroundColor())
            .padding(8.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                drawLine(
                    Color(0x00FFFFFF),
                    Offset(0f, strokeWidth),
                    Offset(0f, size.height),
                    strokeWidth
                )
            },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(songs) { song ->
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = song.art,
                        contentDescription = song.text,
                        modifier = Modifier.width(40.dp).height(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    // title in bold
                    Text(song.title, style = h3TextStyle().copyWithSize(14.sp))
                    // artist in italic
                    Text(song.artist, style = h4TextStyle().copyWithSize(13.sp))
                }
            }
        }
    }
}

