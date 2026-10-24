package com.vauth.foxyvpn.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import com.vauth.foxyvpn.data.FoxyColorTheme
import com.vauth.foxyvpn.data.FoxyUiDesignStyle

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    inversePrimary = md_theme_light_inversePrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    surfaceTint = md_theme_light_surfaceTint,
    surfaceBright = md_theme_light_surfaceBright,
    surfaceDim = md_theme_light_surfaceDim,
    surfaceContainer = md_theme_light_surfaceContainer,
    surfaceContainerHigh = md_theme_light_surfaceContainerHigh,
    surfaceContainerHighest = md_theme_light_surfaceContainerHighest,
    surfaceContainerLow = md_theme_light_surfaceContainerLow,
    surfaceContainerLowest = md_theme_light_surfaceContainerLowest,
    outline = md_theme_light_outline,
    outlineVariant = md_theme_light_outlineVariant,
    inverseSurface = md_theme_light_inverseSurface,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    scrim = md_theme_light_scrim,
)

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    inversePrimary = md_theme_dark_inversePrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    surfaceTint = md_theme_dark_surfaceTint,
    surfaceBright = md_theme_dark_surfaceBright,
    surfaceDim = md_theme_dark_surfaceDim,
    surfaceContainer = md_theme_dark_surfaceContainer,
    surfaceContainerHigh = md_theme_dark_surfaceContainerHigh,
    surfaceContainerHighest = md_theme_dark_surfaceContainerHighest,
    surfaceContainerLow = md_theme_dark_surfaceContainerLow,
    surfaceContainerLowest = md_theme_dark_surfaceContainerLowest,
    outline = md_theme_dark_outline,
    outlineVariant = md_theme_dark_outlineVariant,
    inverseSurface = md_theme_dark_inverseSurface,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    scrim = md_theme_dark_scrim,
)

@Composable
fun FoxyVpnTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    appLanguage: AppLanguage = AppLanguage.EN,
    colorTheme: FoxyColorTheme = FoxyColorTheme.CYBER_FOX,
    uiDesignStyle: FoxyUiDesignStyle = FoxyUiDesignStyle.CLAYMORPHISM,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val systemInDarkTheme = isSystemInDarkTheme()
    val darkTheme = when {
        uiDesignStyle == FoxyUiDesignStyle.DARK_LUXURY -> true
        themeMode == ThemeMode.SYSTEM -> systemInDarkTheme
        themeMode == ThemeMode.LIGHT -> false
        else -> true
    }
    val context = LocalContext.current
    val statusColors = statusColorsForTheme(darkTheme, colorTheme)
    val rawPrimary = when {
        uiDesignStyle == FoxyUiDesignStyle.DARK_LUXURY && colorTheme == FoxyColorTheme.CYBER_FOX -> Color(0xFFE5B84B)
        else -> Color(if (darkTheme) colorTheme.primaryDarkHex else colorTheme.primaryLightHex)
    }
    val targetSecondary = Color(colorTheme.secondaryAccentHex)

    val animatedPrimary by animateColorAsState(
        targetValue = rawPrimary,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "themePrimary",
    )
    val animatedSecondary by animateColorAsState(
        targetValue = targetSecondary,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "themeSecondary",
    )
    val animatedContainer by animateColorAsState(
        targetValue = if (darkTheme) {
            rawPrimary.copy(alpha = 0.24f)
        } else {
            rawPrimary.copy(alpha = 0.16f)
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "themeContainer",
    )

    val baseScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val colorScheme = baseScheme.copy(
        primary = animatedPrimary,
        onPrimary = Color.White,
        primaryContainer = animatedContainer,
        onPrimaryContainer = if (darkTheme) Color(0xFFF8FAFC) else Color(0xFF0F172A),
        secondary = animatedSecondary,
        surfaceTint = animatedPrimary,
    )

    val neuColors = when (uiDesignStyle) {
        FoxyUiDesignStyle.DARK_LUXURY -> NeumorphicColors(
            background = Color(0xFF07080C),
            surface = Color(0xFF10131C),
            surfaceElevated = Color(0xFF171B27),
            shadowLight = Color(0xFFE5B84B),
            shadowDark = Color(0xFF020305),
            accent = animatedPrimary,
            textPrimary = Color(0xFFF9F6EE),
            textSecondary = Color(0xFFB8B2A1),
            isDark = true,
            designStyle = uiDesignStyle,
        )
        FoxyUiDesignStyle.NEO_BRUTALISM -> if (darkTheme) {
            NeumorphicColors(
                background = Color(0xFF0F1219),
                surface = Color(0xFF181D29),
                surfaceElevated = Color(0xFF212838),
                shadowLight = Color(0xFFE2E8F0),
                shadowDark = Color(0xFF050608),
                accent = animatedPrimary,
                textPrimary = Color(0xFFF8FAFC),
                textSecondary = Color(0xFFCBD5E1),
                isDark = true,
                designStyle = uiDesignStyle,
            )
        } else {
            NeumorphicColors(
                background = Color(0xFFF8F6F0),
                surface = Color(0xFFFFFFFF),
                surfaceElevated = Color(0xFFF0EDE4),
                shadowLight = Color(0xFF0F172A),
                shadowDark = Color(0xFF0F172A),
                accent = animatedPrimary,
                textPrimary = Color(0xFF0F172A),
                textSecondary = Color(0xFF334155),
                isDark = false,
                designStyle = uiDesignStyle,
            )
        }
        FoxyUiDesignStyle.BENTO_UI -> if (darkTheme) {
            NeumorphicColors(
                background = Color(0xFF090C12),
                surface = Color(0xFF131823),
                surfaceElevated = Color(0xFF1B2230),
                shadowLight = Color(0xFF2A3447),
                shadowDark = Color(0xFF040609),
                accent = animatedPrimary,
                textPrimary = Color(0xFFF8FAFC),
                textSecondary = Color(0xFF94A3B8),
                isDark = true,
                designStyle = uiDesignStyle,
            )
        } else {
            NeumorphicColors(
                background = Color(0xFFF1F4F9),
                surface = Color(0xFFFFFFFF),
                surfaceElevated = Color(0xFFF7F9FC),
                shadowLight = Color(0xFFFFFFFF),
                shadowDark = Color(0xFFCBD5E1),
                accent = animatedPrimary,
                textPrimary = Color(0xFF0F172A),
                textSecondary = Color(0xFF475569),
                isDark = false,
                designStyle = uiDesignStyle,
            )
        }
        FoxyUiDesignStyle.EDITORIAL_DESIGN -> if (darkTheme) {
            NeumorphicColors(
                background = Color(0xFF0E1013),
                surface = Color(0xFF16191E),
                surfaceElevated = Color(0xFF1F232A),
                shadowLight = Color(0xFF2E3440),
                shadowDark = Color(0xFF060709),
                accent = animatedPrimary,
                textPrimary = Color(0xFFF5F3EE),
                textSecondary = Color(0xFFA8A39A),
                isDark = true,
                designStyle = uiDesignStyle,
            )
        } else {
            NeumorphicColors(
                background = Color(0xFFF5F2E9),
                surface = Color(0xFFFCFBF7),
                surfaceElevated = Color(0xFFEDE9DD),
                shadowLight = Color(0xFFFFFFFF),
                shadowDark = Color(0xFFD6D0C2),
                accent = animatedPrimary,
                textPrimary = Color(0xFF18181B),
                textSecondary = Color(0xFF52525B),
                isDark = false,
                designStyle = uiDesignStyle,
            )
        }
        FoxyUiDesignStyle.CLAYMORPHISM -> if (darkTheme) {
            NeumorphicColors(
                background = Color(0xFF111622),
                surface = Color(0xFF1B2335),
                surfaceElevated = Color(0xFF232D44),
                shadowLight = Color(0xFF3E4F75),
                shadowDark = Color(0xFF070A10),
                accent = animatedPrimary,
                textPrimary = Color(0xFFF8FAFC),
                textSecondary = Color(0xFF94A3B8),
                isDark = true,
                designStyle = uiDesignStyle,
            )
        } else {
            NeumorphicColors(
                background = Color(0xFFEAF0F8),
                surface = Color(0xFFF6F9FE),
                surfaceElevated = Color(0xFFE2EBF6),
                shadowLight = Color(0xFFFFFFFF),
                shadowDark = Color(0xFFBCCAE0),
                accent = animatedPrimary,
                textPrimary = Color(0xFF0F172A),
                textSecondary = Color(0xFF475569),
                isDark = false,
                designStyle = uiDesignStyle,
            )
        }
        FoxyUiDesignStyle.NEUMORPHISM -> if (darkTheme) {
            NeumorphicColors(
                background = NeuDarkBg,
                surface = NeuDarkSurface,
                surfaceElevated = NeuDarkSurfaceElevated,
                shadowLight = NeuDarkShadowLight,
                shadowDark = NeuDarkShadowDark,
                accent = animatedPrimary,
                textPrimary = md_theme_dark_onBackground,
                textSecondary = md_theme_dark_onSurfaceVariant,
                isDark = true,
                designStyle = uiDesignStyle,
            )
        } else {
            NeumorphicColors(
                background = NeuLightBg,
                surface = NeuLightSurface,
                surfaceElevated = NeuLightSurfaceElevated,
                shadowLight = NeuLightShadowLight,
                shadowDark = NeuLightShadowDark,
                accent = animatedPrimary,
                textPrimary = md_theme_light_onBackground,
                textSecondary = md_theme_light_onSurfaceVariant,
                isDark = false,
                designStyle = uiDesignStyle,
            )
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    val strings = foxyStringsFor(appLanguage)
    val fontHierarchy = fontHierarchyFor(appLanguage)
    val typography = foxyTypographyFor(appLanguage)
    val layoutDirection = if (appLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(
        LocalFoxyStatusColors provides statusColors,
        LocalNeumorphicColors provides neuColors,
        LocalFoxyStrings provides strings,
        LocalFoxyFonts provides fontHierarchy,
        LocalLayoutDirection provides layoutDirection,
    ) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            typography = typography,
            motionScheme = MotionScheme.expressive(),
            content = content,
        )
    }
}

