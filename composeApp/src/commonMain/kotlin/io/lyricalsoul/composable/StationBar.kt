package io.lyricalsoul.composable

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.lyricalsoul.radio.RadioManagerStations
import io.lyricalsoul.radio.models.RadioStation
import io.lyricalsoul.ui.Text
import io.lyricalsoul.ui.h2TextStyle
import io.lyricalsoul.ui.h4TextStyle
import io.lyricalsoul.ui.translucentBlack
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.ui.component.copyWithSize
import showoff.composeapp.generated.resources.Res
import showoff.composeapp.generated.resources.compose_multiplatform

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
    Box(
        Modifier.fillMaxWidth().background(translucentBlack()).padding(8.dp)
    ) {
        Box(Modifier.fillMaxWidth().blur(4.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                Modifier.padding(8.dp)
            ) {
                AsyncImage(
                    model = station.logoUrl,
                    placeholder = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = station.name,
                    modifier = Modifier.fillMaxWidth(0.15f),
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                Modifier.fillMaxWidth().scale(0.9f).padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text("You are now listening to", style = h4TextStyle().copyWithSize(12.sp))
                Spacer(modifier = Modifier.height(4.dp))
                // title in bold
                Text(station.name, style = h2TextStyle())
                Spacer(modifier = Modifier.height(2.dp))
                // description
                Text(station.description, style = h4TextStyle())
            }
        }
    }
}