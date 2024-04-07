package net.deechael.esjzone.ui.compose.local

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf


val LocalColorScheme: ProvidableCompositionLocal<ColorScheme> =
    staticCompositionLocalOf { lightColorScheme() }