package net.deechael.esjzone.ui.typegraphy

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import net.deechael.esjzone.ui.font.HarmonyOsSans
import net.deechael.esjzone.ui.font.HarmonyOsSansSC


internal val DefaultLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

internal val Default = TextStyle.Default.copy(
    lineHeightStyle = DefaultLineHeightStyle
)

val HarmonyOsSansTypography = Typography(
    displayLarge = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.0.sp,
        letterSpacing = (-0.2).sp
    ),
    displayMedium = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.0.sp,
        letterSpacing = 0.0.sp
    ),
    displaySmall = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineLarge = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineMedium = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineSmall = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.0.sp,
        letterSpacing = 0.0.sp
    ),
    titleLarge = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.0.sp,
        letterSpacing = 0.0.sp
    ),
    titleMedium = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 0.2.sp
    ),
    titleSmall = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.2.sp
    ),
    bodySmall = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = Default.copy(
        fontFamily = HarmonyOsSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp
    )
)

val HarmonyOsSansSCTypography = Typography(
    displayLarge = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.0.sp,
        letterSpacing = (-0.2).sp
    ),
    displayMedium = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.0.sp,
        letterSpacing = 0.0.sp
    ),
    displaySmall = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineLarge = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineMedium = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.0.sp,
        letterSpacing = 0.0.sp
    ),
    headlineSmall = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.0.sp,
        letterSpacing = 0.0.sp
    ),
    titleLarge = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.0.sp,
        letterSpacing = 0.0.sp
    ),
    titleMedium = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 0.2.sp
    ),
    titleSmall = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.2.sp
    ),
    bodySmall = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = Default.copy(
        fontFamily = HarmonyOsSansSC,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp
    )
)