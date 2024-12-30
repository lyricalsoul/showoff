package io.lyricalsoul.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.jewel.ui.component.Typography

@Composable
actual fun Text(text: String, style: TextStyle) {
    org.jetbrains.jewel.ui.component.Text(text, style = style)
}

@Composable
actual fun h1TextStyle() = Typography.h1TextStyle()

@Composable
actual fun h2TextStyle() = Typography.h2TextStyle()

@Composable
actual fun h3TextStyle() = Typography.h3TextStyle()

@Composable
actual fun h4TextStyle() = Typography.h4TextStyle()

@Composable
actual fun labelTextStyle() = Typography.labelTextStyle()

@Composable
actual fun h0TextStyle() = Typography.h0TextStyle()
