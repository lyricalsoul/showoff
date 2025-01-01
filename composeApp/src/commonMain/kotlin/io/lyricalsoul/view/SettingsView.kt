package io.lyricalsoul.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.lyricalsoul.Showoff
import io.lyricalsoul.ui.*
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider

@Composable
fun SettingsView() {
    Column(
        modifier = Modifier.fillMaxSize().background(backgroundColor()).padding(20.dp),
    ) {
        Text("Settings", style = h1TextStyle())
        Spacer(modifier = Modifier.height(20.dp))

        SettingCategory("Last.fm Scrobbling") {
            Text("Last.fm Scrobbling is not available yet.", style = labelTextStyle())
        }

        SettingCategory("Discord Rich Presence", Showoff().isRPCAvailable) {

        }
    }
}

@Composable
fun SettingCategory(title: String, shouldShow: Boolean, content: @Composable () -> Unit) {
    if (shouldShow) {
        SettingCategory(title, content)
    }
}

@Composable
fun SettingCategory(title: String, content: @Composable () -> Unit) {
    Column {
        Text(title, style = h2TextStyle())
        Spacer(modifier = Modifier.height(20.dp))
        content()
        Spacer(modifier = Modifier.height(20.dp))
        Divider(orientation = Orientation.Horizontal)
    }
}