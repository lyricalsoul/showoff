package io.lyricalsoul.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

// abstract textstyle

@Composable
expect fun Text(text: String, style: TextStyle = labelTextStyle())

@Composable
expect fun h1TextStyle(): TextStyle

@Composable
expect fun h2TextStyle(): TextStyle

@Composable
expect fun h3TextStyle(): TextStyle

@Composable
expect fun h4TextStyle(): TextStyle

@Composable
expect fun labelTextStyle(): TextStyle

@Composable
expect fun h0TextStyle(): TextStyle
