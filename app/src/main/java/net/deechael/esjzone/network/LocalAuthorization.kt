package net.deechael.esjzone.network

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAuthorization: ProvidableCompositionLocal<Authorization> =
    staticCompositionLocalOf { Authorization("", "") }