package io.lyricalsoul.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.window.styling.TitleBarStyle

@Composable
expect fun backgroundColor(): Color

fun getTitleBarColor(): Color {
    return Color(0xCC702963)
}

fun darkenColor(color: Color, factor: Float): Color {
    return Color(
        color.red - factor,
        color.green - factor,
        color.blue - factor,
        color.alpha
    )
}

@Composable
fun texturedBackgroundBrush(): Brush {
    val stl = TitleBarStyle.dark()

    with(LocalDensity.current) {
        return Brush.horizontalGradient(
            0.0f to backgroundColor(),
            0.5f to getTitleBarColor(),
            0.63f to darkenColor(getTitleBarColor(), 0.01f).popBlue(0.1f),
            1.0f to darkenColor(backgroundColor(), 0.01f),
            startX = stl.metrics.gradientStartX.toPx()
        )
    }
}

@Composable
fun invertedTexturedBackgroundBrush(): Brush {
    val stl = TitleBarStyle.dark()

    with(LocalDensity.current) {
        return Brush.horizontalGradient(
            0.0f to backgroundColor(),
            0.5f to darkenColor(getTitleBarColor(), 0.01f).popBlue(0.03f),
            0.65f to getTitleBarColor(),
            1.0f to backgroundColor(),
            startX = stl.metrics.gradientStartX.toPx(),
            endX = stl.metrics.gradientEndX.toPx()
        )
    }
}

private fun Color.popBlue(fl: Float): Color {
    return Color(
        red - fl,
        green - fl,
        blue,
        alpha
    )
}

@Composable
fun borderColor(): Color {
    return Color(0xFFAAAAAA)
}

@Composable
fun translucentBlack(): Color {
    return Color(0x22000000)
}

