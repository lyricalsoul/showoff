package io.lyricalsoul.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import showoff.composeapp.generated.resources.Res
import showoff.composeapp.generated.resources.pause

@Composable
fun PauseButton(isPaused: Boolean, onToggle: () -> Unit) {
    // this is needed because there's no overlap between the ImageVector type and the Painter type. Even if you normalize them to the same type, typechecking will still fail when casting
    val contentDescription = "Play/Pause"
    val modifier = Modifier
        .size(21.dp)
        .clickable(onClick = onToggle)

    val colorFilter = ColorFilter.tint(
        Color.White
    )

    if (isPaused) {
        Image(
            Icons.Filled.PlayArrow,
            contentDescription = contentDescription,
            modifier = modifier,
            colorFilter = colorFilter
        )
    } else {
        Image(
            painterResource(Res.drawable.pause),
            contentDescription = contentDescription,
            modifier = modifier,
            colorFilter = colorFilter
        )
    }
}