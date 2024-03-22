package net.deechael.esjzone.ui.navigation

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.Navigator

val LocalBaseNavigator: ProvidableCompositionLocal<Navigator> =
    staticCompositionLocalOf { throw RuntimeException() }