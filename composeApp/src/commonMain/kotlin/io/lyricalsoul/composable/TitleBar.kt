package io.lyricalsoul.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.lyricalsoul.radio.RadioManagerStations
import io.lyricalsoul.ui.getTitleBarColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.window.DecoratedWindowScope
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.newFullscreenControls
import org.jetbrains.jewel.window.styling.TitleBarStyle
import showoff.composeapp.generated.resources.Res
import showoff.composeapp.generated.resources.github
import java.awt.Desktop
import java.net.URI


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DecoratedWindowScope.TitleBarView() {
    TitleBar(
        Modifier.newFullscreenControls(), gradientStartColor = getTitleBarColor(),
        style = TitleBarStyle.dark()
    ) {
        Row(Modifier.align(Alignment.Start)) {
            Dropdown(
                Modifier.height(30.dp).padding(2.dp),
                menuContent = {
                    RadioManagerStations.stations.forEach {
                        selectableItem(
                            selected = RadioManagerStations.currentStation == it,
                            onClick = { RadioManagerStations.currentStation = it }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(painterResource(it.smallLogoDrawable), it.name, modifier = Modifier.size(20.dp))
                                Text(it.name)
                            }
                        }
                    }
                },
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painterResource(RadioManagerStations.currentStation.smallLogoDrawable),
                            null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(RadioManagerStations.currentStation.name)
                    }
                }
            }
        }

        Text(title)

        Row(Modifier.align(Alignment.End)) {
            Tooltip({ Text("GitHub Repo") }) {
                IconButton(
                    { Desktop.getDesktop().browse(URI.create("https://github.com/lyricalsoul/showoff")) },
                    Modifier.size(40.dp).padding(5.dp),
                ) {
                    Icon(painterResource(Res.drawable.github), null, modifier = Modifier.size(20.dp))
                }
            }

        }
    }
}