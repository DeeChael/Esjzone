package net.deechael.esjzone.network

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.Navigator

public val LocalAuthorization: ProvidableCompositionLocal<Authorization> =
    staticCompositionLocalOf { Authorization("", "") }