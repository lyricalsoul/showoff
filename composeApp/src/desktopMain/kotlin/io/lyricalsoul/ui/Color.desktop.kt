package io.lyricalsoul.ui

import androidx.compose.runtime.Composable
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition

@Composable
actual fun backgroundColor() = JewelTheme.darkThemeDefinition().globalColors.panelBackground