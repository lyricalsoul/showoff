package io.lyricalsoul.viewmodel

import androidx.compose.runtime.Composable

data class AppView(
    val title: String,
    val content: @Composable () -> Unit
)
