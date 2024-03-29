package net.deechael.esjzone

import androidx.compose.runtime.mutableStateOf
import net.deechael.esjzone.ui.theme.catppuccin.CatppuccinThemeType

object GlobalSettings {

    var adult = mutableStateOf(true)

    var theme = mutableStateOf(CatppuccinThemeType.LATTE_YELLOW)

}