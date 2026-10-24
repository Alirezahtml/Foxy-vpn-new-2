package com.vauth.foxyvpn.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.vauth.foxyvpn.data.FoxyColorTheme

val FoxSeed = Color(0xFFE05324)

// Refined 2026 Neumorphic Pearl-Alabaster Light Palette (soft, crisp, zero eye-strain)
val NeuLightBg = Color(0xFFEDF1F7)
val NeuLightSurface = Color(0xFFEDF1F7)
val NeuLightSurfaceElevated = Color(0xFFF5F8FC)
val NeuLightShadowLight = Color(0xFFFFFFFF)
val NeuLightShadowDark = Color(0xFFC4CFE0)

val md_theme_light_primary = Color(0xFFE05324)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFDBD0)
val md_theme_light_onPrimaryContainer = Color(0xFF3A0B00)
val md_theme_light_inversePrimary = Color(0xFFFFB59D)

val md_theme_light_secondary = Color(0xFF4B5D73)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFD5E3F7)
val md_theme_light_onSecondaryContainer = Color(0xFF0E1D2E)

val md_theme_light_tertiary = Color(0xFF0D9488)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFCCFBF1)
val md_theme_light_onTertiaryContainer = Color(0xFF042F2E)

val md_theme_light_error = Color(0xFFDC2626)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_errorContainer = Color(0xFFFEE2E2)
val md_theme_light_onErrorContainer = Color(0xFF450A0A)

val md_theme_light_background = NeuLightBg
val md_theme_light_onBackground = Color(0xFF0F172A)
val md_theme_light_surface = NeuLightSurface
val md_theme_light_onSurface = Color(0xFF0F172A)
val md_theme_light_surfaceVariant = Color(0xFFE2E8F0)
val md_theme_light_onSurfaceVariant = Color(0xFF475569)
val md_theme_light_surfaceTint = md_theme_light_primary

val md_theme_light_surfaceContainerLowest = Color(0xFFFFFFFF)
val md_theme_light_surfaceContainerLow = Color(0xFFF4F7FB)
val md_theme_light_surfaceContainer = NeuLightSurface
val md_theme_light_surfaceContainerHigh = Color(0xFFE5EBF3)
val md_theme_light_surfaceContainerHighest = Color(0xFFDCE4EE)
val md_theme_light_surfaceBright = Color(0xFFF9FBFE)
val md_theme_light_surfaceDim = Color(0xFFD5DFEB)

val md_theme_light_outline = Color(0xFF94A3B8)
val md_theme_light_outlineVariant = Color(0xFFCBD5E1)
val md_theme_light_inverseSurface = Color(0xFF1E293B)
val md_theme_light_inverseOnSurface = Color(0xFFF1F5F9)
val md_theme_light_scrim = Color(0xFF000000)

// Refined 2026 Neumorphic Midnight-Obsidian Dark Palette (deep, rich, high-contrast velvet)
val NeuDarkBg = Color(0xFF131722)
val NeuDarkSurface = Color(0xFF181D2A)
val NeuDarkSurfaceElevated = Color(0xFF202737)
val NeuDarkShadowLight = Color(0xFF273044)
val NeuDarkShadowDark = Color(0xFF0A0D14)

val md_theme_dark_primary = Color(0xFFFF7849)
val md_theme_dark_onPrimary = Color(0xFF451200)
val md_theme_dark_primaryContainer = Color(0xFF692207)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFDBD0)
val md_theme_dark_inversePrimary = Color(0xFFE05324)

val md_theme_dark_secondary = Color(0xFF94A3B8)
val md_theme_dark_onSecondary = Color(0xFF0F172A)
val md_theme_dark_secondaryContainer = Color(0xFF283245)
val md_theme_dark_onSecondaryContainer = Color(0xFFE2E8F0)

val md_theme_dark_tertiary = Color(0xFF2DD4BF)
val md_theme_dark_onTertiary = Color(0xFF042F2E)
val md_theme_dark_tertiaryContainer = Color(0xFF115E59)
val md_theme_dark_onTertiaryContainer = Color(0xFFCCFBF1)

val md_theme_dark_error = Color(0xFFF87171)
val md_theme_dark_onError = Color(0xFF450A0A)
val md_theme_dark_errorContainer = Color(0xFF7F1D1D)
val md_theme_dark_onErrorContainer = Color(0xFFFEE2E2)

val md_theme_dark_background = NeuDarkBg
val md_theme_dark_onBackground = Color(0xFFF1F5F9)
val md_theme_dark_surface = NeuDarkSurface
val md_theme_dark_onSurface = Color(0xFFF1F5F9)
val md_theme_dark_surfaceVariant = Color(0xFF232B3B)
val md_theme_dark_onSurfaceVariant = Color(0xFF94A3B8)
val md_theme_dark_surfaceTint = md_theme_dark_primary

val md_theme_dark_surfaceContainerLowest = Color(0xFF0D1017)
val md_theme_dark_surfaceContainerLow = Color(0xFF151A25)
val md_theme_dark_surfaceContainer = NeuDarkSurface
val md_theme_dark_surfaceContainerHigh = Color(0xFF212838)
val md_theme_dark_surfaceContainerHighest = Color(0xFF293246)
val md_theme_dark_surfaceBright = Color(0xFF333E54)
val md_theme_dark_surfaceDim = Color(0xFF10141D)

val md_theme_dark_outline = Color(0xFF475569)
val md_theme_dark_outlineVariant = Color(0xFF2A3447)
val md_theme_dark_inverseSurface = Color(0xFFF1F5F9)
val md_theme_dark_inverseOnSurface = Color(0xFF181D2A)
val md_theme_dark_scrim = Color(0xFF000000)

data class FoxyStatusColors(
    val connected: Color,
    val connecting: Color,
    val disconnected: Color,
)

val LightStatusColors = FoxyStatusColors(
    connected = Color(0xFF059669),
    connecting = Color(0xFFD97706),
    disconnected = Color(0xFF64748B),
)

val DarkStatusColors = FoxyStatusColors(
    connected = Color(0xFF34D399),
    connecting = Color(0xFFFBBF24),
    disconnected = Color(0xFF94A3B8),
)

fun statusColorsForTheme(darkTheme: Boolean, colorTheme: FoxyColorTheme): FoxyStatusColors {
    val secondary = Color(colorTheme.secondaryAccentHex)
    return if (darkTheme) {
        DarkStatusColors.copy(
            connected = lerp( Color(0xFF34D399), secondary, 0.25f),
        )
    } else {
        LightStatusColors.copy(
            connected = lerp(Color(0xFF059669), secondary, 0.22f),
        )
    }
}

val LocalFoxyStatusColors = staticCompositionLocalOf { LightStatusColors }
