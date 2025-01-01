package io.lyricalsoul.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.lyricalsoul.components.player.NowPlayingCard
import io.lyricalsoul.components.player.PreviouslyPlayedList
import io.lyricalsoul.components.player.StationBar
import io.lyricalsoul.ui.backgroundColor
import io.lyricalsoul.ui.darkenColor
import io.lyricalsoul.ui.getTitleBarColor
import io.lyricalsoul.ui.texturedBackgroundBrush
import io.lyricalsoul.viewmodel.MainViewModel

@Composable
fun PlayerView() {
    Column(
        Modifier.fillMaxSize().background(backgroundColor()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
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
                    .border(Dp.Hairline, darkenColor(getTitleBarColor(), 0.01f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                MainViewModel.nowPlayingSong?.let {
                    Box(Modifier.fillMaxSize().blur(8.dp))
                    NowPlayingCard(it, MainViewModel.currentStation!!)
                }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                if (MainViewModel.latestPlayedTracks.isNotEmpty()) {
                    PreviouslyPlayedList(MainViewModel.latestPlayedTracks)
                }
            }
        }

        Box(
            Modifier.fillMaxHeight()
        ) {
            StationBar(
                MainViewModel.currentStation!!,
                MainViewModel.listenerInfo
            )
        }
    }
}