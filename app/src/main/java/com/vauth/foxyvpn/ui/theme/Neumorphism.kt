package com.vauth.foxyvpn.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vauth.foxyvpn.data.FoxyUiDesignStyle
import com.vauth.foxyvpn.data.model.ConnectionState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class NeumorphicColors(
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val shadowLight: Color,
    val shadowDark: Color,
    val accent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val isDark: Boolean,
    val designStyle: FoxyUiDesignStyle = FoxyUiDesignStyle.CLAYMORPHISM,
)

val LocalNeumorphicColors = staticCompositionLocalOf {
    NeumorphicColors(
        background = NeuLightBg,
        surface = NeuLightSurface,
        surfaceElevated = NeuLightSurfaceElevated,
        shadowLight = NeuLightShadowLight,
        shadowDark = NeuLightShadowDark,
        accent = md_theme_light_primary,
        textPrimary = md_theme_light_onBackground,
        textSecondary = md_theme_light_onSurfaceVariant,
        isDark = false,
        designStyle = FoxyUiDesignStyle.CLAYMORPHISM,
    )
}

val currentNeumorphicColors: NeumorphicColors
    @Composable
    @ReadOnlyComposable
    get() = LocalNeumorphicColors.current

/**
 * Renders style-adaptive outer shadow / bevel effects for all 6 UI Design Styles.
 */
fun Modifier.neumorphicRaised(
    shape: Shape = RoundedCornerShape(16.dp),
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 6.dp,
    isPressed: Boolean = false,
    lightShadowColor: Color? = null,
    darkShadowColor: Color? = null,
    designStyle: FoxyUiDesignStyle = FoxyUiDesignStyle.NEUMORPHISM,
    isDark: Boolean = false,
    accentColor: Color = Color(0xFFFF6D00),
): Modifier = this.then(
    Modifier.drawBehind {
        if (size.width <= 2f || size.height <= 2f || !size.width.isFinite() || !size.height.isFinite()) return@drawBehind
        val radiusPx = cornerRadius.toPx().coerceAtLeast(0f)

        when (designStyle) {
            FoxyUiDesignStyle.NEO_BRUTALISM -> {
                // Hard offset zero-blur brutalist shadow
                val hardOffset = (if (isPressed) 1.5.dp else 4.2.dp).toPx()
                val shadowColor = if (isDark) accentColor.copy(alpha = 0.85f) else Color(0xFF111111)
                drawRoundRect(
                    color = shadowColor,
                    topLeft = Offset(hardOffset, hardOffset),
                    size = size,
                    cornerRadius = CornerRadius(radiusPx, radiusPx),
                )
            }
            FoxyUiDesignStyle.BENTO_UI -> {
                val dropOffset = (if (isPressed) 1.dp else 3.dp).toPx()
                drawRoundRect(
                    color = Color.Black.copy(alpha = if (isDark) 0.32f else 0.07f),
                    topLeft = Offset(0f, dropOffset),
                    size = size,
                    cornerRadius = CornerRadius(radiusPx, radiusPx),
                )
            }
            FoxyUiDesignStyle.EDITORIAL_DESIGN -> {
                val dropOffset = (if (isPressed) 1.dp else 2.5.dp).toPx()
                drawRoundRect(
                    color = Color.Black.copy(alpha = if (isDark) 0.28f else 0.06f),
                    topLeft = Offset(dropOffset * 0.5f, dropOffset),
                    size = size,
                    cornerRadius = CornerRadius(radiusPx, radiusPx),
                )
            }
            FoxyUiDesignStyle.DARK_LUXURY -> {
                val glowRadius = (if (isPressed) 2.dp else 5.dp).toPx()
                drawRoundRect(
                    color = Color.Black.copy(alpha = if (isDark) 0.55f else 0.14f),
                    topLeft = Offset(0f, glowRadius * 0.65f),
                    size = size,
                    cornerRadius = CornerRadius(radiusPx, radiusPx),
                )
            }
            FoxyUiDesignStyle.CLAYMORPHISM,
            FoxyUiDesignStyle.NEUMORPHISM -> {
                val lightColor = lightShadowColor ?: if (drawContext.size.width > 0) Color.White.copy(alpha = 0.9f) else Color.White
                val darkColor = darkShadowColor ?: Color.Black.copy(alpha = 0.15f)

                val currentElev = if (isPressed) (elevation * 0.25f) else elevation
                val blurPx = currentElev.toPx()
                val offsetPx = (currentElev * 0.85f).toPx()

                drawIntoCanvas { canvas ->
                    if (blurPx > 0.5f && blurPx.isFinite()) {
                        val paintLight = Paint().asFrameworkPaint().apply {
                            color = lightColor.toArgb()
                            isAntiAlias = true
                            maskFilter = BlurMaskFilter(blurPx.coerceAtLeast(1f), BlurMaskFilter.Blur.NORMAL)
                        }
                        canvas.nativeCanvas.drawRoundRect(
                            -offsetPx,
                            -offsetPx,
                            size.width - offsetPx,
                            size.height - offsetPx,
                            radiusPx,
                            radiusPx,
                            paintLight,
                        )

                        val paintDark = Paint().asFrameworkPaint().apply {
                            color = darkColor.toArgb()
                            isAntiAlias = true
                            maskFilter = BlurMaskFilter(blurPx.coerceAtLeast(1f), BlurMaskFilter.Blur.NORMAL)
                        }
                        canvas.nativeCanvas.drawRoundRect(
                            offsetPx,
                            offsetPx,
                            size.width + offsetPx,
                            size.height + offsetPx,
                            radiusPx,
                            radiusPx,
                            paintDark,
                        )
                    }
                }
            }
        }
    }
)

/**
 * Renders inner clay 3D inflation bevel or luxury/editorial inner accents.
 */
fun Modifier.styleInnerTreatment(
    designStyle: FoxyUiDesignStyle,
    cornerRadius: Dp,
    isDark: Boolean,
    accentColor: Color,
): Modifier = this.then(
    Modifier.drawBehind {
        val w = size.width
        val h = size.height
        if (w <= 4f || h <= 4f || !w.isFinite() || !h.isFinite()) return@drawBehind
        val r = cornerRadius.toPx().coerceAtLeast(0f)

        when (designStyle) {
            FoxyUiDesignStyle.CLAYMORPHISM -> {
                // Glossy inflated 3D inner top-left highlight + bottom-right clay depth
                val bevel = 4.5.dp.toPx().coerceAtMost(minOf(w, h) * 0.25f)
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.16f else 0.72f),
                            Color.Transparent,
                            Color.Black.copy(alpha = if (isDark) 0.34f else 0.09f),
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(w, h),
                    ),
                    size = size,
                    cornerRadius = CornerRadius(r, r),
                )
                drawRoundRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.08f else 0.38f),
                            Color.Transparent,
                        ),
                        center = Offset(w * 0.28f, h * 0.22f),
                        radius = maxOf(w, h) * 0.65f,
                    ),
                    topLeft = Offset(bevel * 0.3f, bevel * 0.3f),
                    size = Size((w - bevel * 0.6f).coerceAtLeast(1f), (h - bevel * 0.6f).coerceAtLeast(1f)),
                    cornerRadius = CornerRadius(r, r),
                )
            }
            FoxyUiDesignStyle.DARK_LUXURY -> {
                // Subtle champagne gold / luxury top sheen
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = if (isDark) 0.12f else 0.07f),
                            Color.Transparent,
                            Color.Black.copy(alpha = if (isDark) 0.25f else 0.03f),
                        ),
                    ),
                    size = size,
                    cornerRadius = CornerRadius(r, r),
                )
            }
            FoxyUiDesignStyle.BENTO_UI -> {
                // Crisp Bento glass top highlight
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.06f else 0.55f),
                            Color.Transparent,
                        ),
                        startY = 0f,
                        endY = h * 0.45f,
                    ),
                    size = size,
                    cornerRadius = CornerRadius(r, r),
                )
            }
            FoxyUiDesignStyle.EDITORIAL_DESIGN -> {
                // Architectural top rule accent line
                val lineH = 2.dp.toPx()
                drawRect(
                    color = accentColor.copy(alpha = if (isDark) 0.42f else 0.32f),
                    topLeft = Offset(r * 0.6f, 0f),
                    size = Size((w - r * 1.2f).coerceAtLeast(1f), lineH),
                )
            }
            else -> Unit
        }
    }
)

/**
 * Renders a sunken / inset neumorphic groove for sliders, textboxes, and tracks.
 */
fun Modifier.neumorphicSunken(
    shape: Shape = RoundedCornerShape(16.dp),
    cornerRadius: Dp = 16.dp,
    depth: Dp = 4.dp,
    surfaceColor: Color = Color.Transparent,
): Modifier = this.then(
    Modifier
        .clip(shape)
        .drawBehind {
            val w = size.width
            val h = size.height
            if (w <= 2f || h <= 2f || !w.isFinite() || !h.isFinite()) return@drawBehind
            val r = cornerRadius.toPx().coerceAtLeast(0f)
            val d = depth.toPx().coerceAtLeast(1f)

            // Subtle base fill
            if (surfaceColor != Color.Transparent) {
                drawRoundRect(
                    color = surfaceColor,
                    size = size,
                    cornerRadius = CornerRadius(r, r),
                )
            }

            // Top-left inset dark shadow
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.14f), Color.Transparent),
                    start = Offset(0f, 0f),
                    end = Offset((d * 2.5f).coerceAtLeast(2f), (d * 2.5f).coerceAtLeast(2f)),
                ),
                size = size,
                cornerRadius = CornerRadius(r, r),
            )

            // Bottom-right inset light highlight
            val startX = (w - d * 2f).coerceAtMost(w - 2f)
            val startY = (h - d * 2f).coerceAtMost(h - 2f)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.25f)),
                    start = Offset(startX, startY),
                    end = Offset(w, h),
                ),
                size = size,
                cornerRadius = CornerRadius(r, r),
            )
        }
)

@Composable
fun NeumorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 6.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val neu = currentNeumorphicColors
    val style = neu.designStyle
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val effectiveCorner = when (style) {
        FoxyUiDesignStyle.CLAYMORPHISM -> cornerRadius * 1.15f
        FoxyUiDesignStyle.NEO_BRUTALISM -> (cornerRadius * 0.65f).coerceAtLeast(10.dp)
        FoxyUiDesignStyle.BENTO_UI -> (cornerRadius * 1.15f).coerceAtLeast(22.dp)
        FoxyUiDesignStyle.EDITORIAL_DESIGN -> (cornerRadius * 0.45f).coerceAtLeast(8.dp)
        FoxyUiDesignStyle.DARK_LUXURY -> cornerRadius
        FoxyUiDesignStyle.NEUMORPHISM -> cornerRadius
    }

    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed) elevation * 0.35f else elevation,
        animationSpec = spring(stiffness = 400f),
        label = "neu-card-elevation",
    )

    val shape = RoundedCornerShape(effectiveCorner)

    val borderWidth = when (style) {
        FoxyUiDesignStyle.NEO_BRUTALISM -> 2.2.dp
        FoxyUiDesignStyle.CLAYMORPHISM -> 1.5.dp
        FoxyUiDesignStyle.DARK_LUXURY -> 1.2.dp
        FoxyUiDesignStyle.BENTO_UI -> 1.2.dp
        FoxyUiDesignStyle.EDITORIAL_DESIGN -> 1.dp
        FoxyUiDesignStyle.NEUMORPHISM -> 1.dp
    }

    val borderColor = when (style) {
        FoxyUiDesignStyle.NEO_BRUTALISM -> if (neu.isDark) neu.accent.copy(alpha = 0.9f) else Color(0xFF111111)
        FoxyUiDesignStyle.DARK_LUXURY -> neu.accent.copy(alpha = if (neu.isDark) 0.38f else 0.28f)
        FoxyUiDesignStyle.CLAYMORPHISM -> if (neu.isDark) Color.White.copy(alpha = 0.14f) else Color.White.copy(alpha = 0.85f)
        FoxyUiDesignStyle.BENTO_UI -> if (neu.isDark) Color.White.copy(alpha = 0.10f) else Color.Black.copy(alpha = 0.08f)
        FoxyUiDesignStyle.EDITORIAL_DESIGN -> if (neu.isDark) Color.White.copy(alpha = 0.14f) else Color.Black.copy(alpha = 0.14f)
        FoxyUiDesignStyle.NEUMORPHISM -> if (neu.isDark) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.6f)
    }

    Box(
        modifier = modifier
            .neumorphicRaised(
                shape = shape,
                cornerRadius = effectiveCorner,
                elevation = animatedElevation,
                isPressed = isPressed,
                lightShadowColor = neu.shadowLight.copy(alpha = if (neu.isDark) 0.3f else 0.85f),
                darkShadowColor = neu.shadowDark.copy(alpha = if (neu.isDark) 0.7f else 0.45f),
                designStyle = style,
                isDark = neu.isDark,
                accentColor = neu.accent,
            )
            .clip(shape)
            .background(neu.surface)
            .styleInnerTreatment(
                designStyle = style,
                cornerRadius = effectiveCorner,
                isDark = neu.isDark,
                accentColor = neu.accent,
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape,
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        role = Role.Button,
                        onClick = onClick,
                    )
                } else Modifier
            ),
    ) {
        content()
    }
}

@Composable
fun NeumorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 5.dp,
    accentColor: Color? = null,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val neu = currentNeumorphicColors
    val style = neu.designStyle
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptics = LocalHapticFeedback.current

    val effectiveCorner = when (style) {
        FoxyUiDesignStyle.CLAYMORPHISM -> cornerRadius * 1.15f
        FoxyUiDesignStyle.NEO_BRUTALISM -> (cornerRadius * 0.6f).coerceAtLeast(8.dp)
        FoxyUiDesignStyle.EDITORIAL_DESIGN -> (cornerRadius * 0.45f).coerceAtLeast(6.dp)
        else -> cornerRadius
    }

    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed && enabled) 1.5.dp else elevation,
        label = "btn-elev",
    )
    val shape = RoundedCornerShape(effectiveCorner)
    val bgColor = accentColor ?: neu.surface

    val borderWidth = if (style == FoxyUiDesignStyle.NEO_BRUTALISM) 2.dp else 1.dp
    val borderColor = when (style) {
        FoxyUiDesignStyle.NEO_BRUTALISM -> if (neu.isDark) neu.accent else Color(0xFF111111)
        FoxyUiDesignStyle.DARK_LUXURY -> neu.accent.copy(alpha = 0.40f)
        else -> if (neu.isDark) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.7f)
    }

    Box(
        modifier = modifier
            .neumorphicRaised(
                shape = shape,
                cornerRadius = effectiveCorner,
                elevation = animatedElevation,
                isPressed = isPressed && enabled,
                lightShadowColor = neu.shadowLight.copy(alpha = if (neu.isDark) 0.35f else 0.9f),
                darkShadowColor = neu.shadowDark.copy(alpha = if (neu.isDark) 0.75f else 0.5f),
                designStyle = style,
                isDark = neu.isDark,
                accentColor = neu.accent,
            )
            .clip(shape)
            .background(bgColor)
            .styleInnerTreatment(
                designStyle = style,
                cornerRadius = effectiveCorner,
                isDark = neu.isDark,
                accentColor = neu.accent,
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape,
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
            )
            .padding(horizontal = 22.dp, vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun NeumorphicIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
    isSelected: Boolean = false,
    content: @Composable () -> Unit,
) {
    val neu = currentNeumorphicColors
    val style = neu.designStyle
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptics = LocalHapticFeedback.current

    val shape = when (style) {
        FoxyUiDesignStyle.NEO_BRUTALISM -> RoundedCornerShape(12.dp)
        FoxyUiDesignStyle.BENTO_UI -> RoundedCornerShape(14.dp)
        FoxyUiDesignStyle.EDITORIAL_DESIGN -> RoundedCornerShape(10.dp)
        else -> CircleShape
    }
    val cornerRadius = when (style) {
        FoxyUiDesignStyle.NEO_BRUTALISM -> 12.dp
        FoxyUiDesignStyle.BENTO_UI -> 14.dp
        FoxyUiDesignStyle.EDITORIAL_DESIGN -> 10.dp
        else -> size / 2
    }

    val elevation by animateDpAsState(
        targetValue = if (isPressed || isSelected) 1.dp else 4.dp,
        label = "icon-btn-elev",
    )

    val borderWidth = if (style == FoxyUiDesignStyle.NEO_BRUTALISM) 1.8.dp else 1.dp
    val borderColor = when {
        isSelected -> neu.accent
        style == FoxyUiDesignStyle.NEO_BRUTALISM -> if (neu.isDark) neu.accent.copy(alpha = 0.85f) else Color(0xFF111111)
        style == FoxyUiDesignStyle.DARK_LUXURY -> neu.accent.copy(alpha = if (neu.isDark) 0.35f else 0.25f)
        neu.isDark -> Color.White.copy(alpha = 0.08f)
        else -> Color.White.copy(alpha = 0.78f)
    }

    Box(
        modifier = modifier
            .size(size)
            .neumorphicRaised(
                shape = shape,
                cornerRadius = cornerRadius,
                elevation = elevation,
                isPressed = isPressed || isSelected,
                lightShadowColor = neu.shadowLight.copy(alpha = if (neu.isDark) 0.35f else 0.9f),
                darkShadowColor = neu.shadowDark.copy(alpha = if (neu.isDark) 0.75f else 0.5f),
                designStyle = style,
                isDark = neu.isDark,
                accentColor = neu.accent,
            )
            .clip(shape)
            .background(if (isSelected) neu.accent.copy(alpha = 0.16f) else neu.surface)
            .styleInnerTreatment(
                designStyle = style,
                cornerRadius = cornerRadius,
                isDark = neu.isDark,
                accentColor = neu.accent,
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape,
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/**
 * Slide-to-Connect Neumorphic Component.
 * - Distinct gesture to Connect (drag thumb to end).
 * - Distinct gesture to Disconnect (drag thumb to start).
 * - Independent visual feedback and animations for Ready, Connecting, Connected, Disconnecting, Error.
 */
@Composable
fun NeumorphicSlideToConnect(
    state: ConnectionState,
    lastError: String?,
    onRequestConnect: () -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val neu = currentNeumorphicColors
    val statusColors = LocalFoxyStatusColors.current
    val s = currentFoxyStrings
    val haptics = LocalHapticFeedback.current
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val trackHeight = 68.dp
    val thumbSize = 56.dp
    val trackPadding = 6.dp
    val thumbRadius = 28.dp
    val trackCornerRadius = 34.dp

    var totalWidthPx by remember { mutableFloatStateOf(0f) }
    val maxDragPx = remember(totalWidthPx, density) {
        with(density) {
            (totalWidthPx - (thumbSize + trackPadding * 2).toPx()).coerceAtLeast(0f)
        }
    }

    // Drag offset in px
    val offsetX = remember { Animatable(0f) }

    var isDisconnecting by remember { mutableStateOf(false) }

    // When connection state changes, reset or position thumb
    LaunchedEffect(state, maxDragPx) {
        if (state != ConnectionState.CONNECTED) {
            isDisconnecting = false
        }
        if (maxDragPx > 0f) {
            when (state) {
                ConnectionState.CONNECTED -> {
                    offsetX.animateTo(maxDragPx, spring(stiffness = 300f))
                }
                ConnectionState.CONNECTING -> {
                    offsetX.animateTo(maxDragPx, spring(stiffness = 300f))
                }
                ConnectionState.DISCONNECTED -> {
                    offsetX.animateTo(0f, spring(stiffness = 400f))
                }
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "slide-pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse-alpha",
    )

    val hasError = state == ConnectionState.DISCONNECTED && !lastError.isNullOrBlank()
    val errorColor = MaterialTheme.colorScheme.error

    val trackBgColor = when {
        isDisconnecting -> statusColors.connecting.copy(alpha = 0.14f)
        state == ConnectionState.CONNECTED -> statusColors.connected.copy(alpha = 0.12f)
        state == ConnectionState.CONNECTING -> statusColors.connecting.copy(alpha = 0.15f)
        hasError -> errorColor.copy(alpha = 0.10f)
        else -> neu.surface
    }

    val thumbAccentColor = when {
        isDisconnecting -> statusColors.connecting
        state == ConnectionState.CONNECTED -> statusColors.connected
        state == ConnectionState.CONNECTING -> statusColors.connecting
        hasError -> errorColor
        else -> neu.accent
    }

    val trackShape = RoundedCornerShape(trackCornerRadius)
    val thumbShape = CircleShape

    val dragProgress = if (maxDragPx > 0f) (offsetX.value / maxDragPx).coerceIn(0f, 1f) else 0f

    val dragModifier = Modifier.draggable(
        orientation = Orientation.Horizontal,
        reverseDirection = s.isRtl,
        enabled = state != ConnectionState.CONNECTING && !isDisconnecting,
        state = rememberDraggableState { delta ->
            if (maxDragPx > 0f) {
                coroutineScope.launch {
                    val newOffset = (offsetX.value + delta).coerceIn(0f, maxDragPx)
                    offsetX.snapTo(newOffset)
                }
            }
        },
        onDragStopped = {
            if (maxDragPx > 0f) {
                when (state) {
                    ConnectionState.DISCONNECTED -> {
                        if (offsetX.value >= maxDragPx * 0.55f) {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            offsetX.animateTo(maxDragPx, spring(stiffness = 500f))
                            onRequestConnect()
                            kotlinx.coroutines.delay(350)
                            if (state == ConnectionState.DISCONNECTED) {
                                offsetX.animateTo(0f, spring(stiffness = 400f))
                            }
                        } else {
                            offsetX.animateTo(0f, spring(stiffness = 500f))
                        }
                    }
                    ConnectionState.CONNECTED -> {
                        if (offsetX.value <= maxDragPx * 0.45f) {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            isDisconnecting = true
                            offsetX.animateTo(0f, spring(stiffness = 500f))
                            onDisconnect()
                            kotlinx.coroutines.delay(400)
                            isDisconnecting = false
                        } else {
                            offsetX.animateTo(maxDragPx, spring(stiffness = 500f))
                        }
                    }
                    ConnectionState.CONNECTING -> {
                        // During connecting, allow tap to cancel
                    }
                }
            }
        },
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(trackHeight)
            .onSizeChanged { totalWidthPx = it.width.toFloat() }
            .then(dragModifier)
            .neumorphicRaised(
                shape = trackShape,
                cornerRadius = trackCornerRadius,
                elevation = 5.dp,
                lightShadowColor = neu.shadowLight.copy(alpha = if (neu.isDark) 0.35f else 0.85f),
                darkShadowColor = neu.shadowDark.copy(alpha = if (neu.isDark) 0.75f else 0.45f),
            )
            .clip(trackShape)
            .background(neu.surface)
            .border(
                width = 1.dp,
                color = when {
                    hasError -> errorColor.copy(alpha = 0.45f)
                    neu.isDark -> Color.White.copy(alpha = 0.05f)
                    else -> Color.White.copy(alpha = 0.65f)
                },
                shape = trackShape,
            )
            .neumorphicSunken(
                shape = trackShape,
                cornerRadius = trackCornerRadius,
                depth = 3.dp,
                surfaceColor = trackBgColor,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        // Track text and guidance
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            when {
                isDisconnecting -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 48.dp),
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 2.5.dp,
                            modifier = Modifier.size(18.dp),
                            color = statusColors.connecting,
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = s.disconnecting,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = statusColors.connecting,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                }
                state == ConnectionState.DISCONNECTED -> {
                    val labelAlpha = (1f - dragProgress * 1.4f).coerceIn(0f, 1f)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 60.dp, end = 12.dp)
                            .graphicsLayer { alpha = labelAlpha },
                    ) {
                        Text(
                            text = if (hasError) s.slideToRetry else s.slideToConnect,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.2.sp,
                            ),
                            color = if (hasError) errorColor else neu.textPrimary.copy(alpha = 0.88f),
                            maxLines = 1,
                            softWrap = false,
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = (if (hasError) errorColor else neu.accent).copy(alpha = pulseAlpha),
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
                state == ConnectionState.CONNECTING -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 60.dp, start = 12.dp)
                            .clickable {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                onDisconnect()
                            },
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 2.5.dp,
                            modifier = Modifier.size(18.dp),
                            color = statusColors.connecting,
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = s.connectingTapToCancel,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = statusColors.connecting,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                }
                state == ConnectionState.CONNECTED -> {
                    val labelAlpha = (dragProgress * 1.4f - 0.4f).coerceIn(0f, 1f)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 60.dp, start = 12.dp)
                            .graphicsLayer { alpha = labelAlpha },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = statusColors.connected.copy(alpha = pulseAlpha),
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = s.slideToDisconnect,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.2.sp,
                            ),
                            color = statusColors.connected,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                }
            }
        }

        // Draggable / Tappable Thumb
        // Note: Modifier.offset { IntOffset(x, y) } automatically mirrors x in RTL LayoutDirection!
        // Never manually negate currentOffsetPx for RTL or it will double-invert.
        val currentOffsetPx = offsetX.value

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(currentOffsetPx.roundToInt(), 0)
                }
                .padding(trackPadding)
                .size(thumbSize)
                .neumorphicRaised(
                    shape = thumbShape,
                    cornerRadius = thumbRadius,
                    elevation = 6.dp,
                    lightShadowColor = neu.shadowLight.copy(alpha = if (neu.isDark) 0.4f else 0.95f),
                    darkShadowColor = neu.shadowDark.copy(alpha = if (neu.isDark) 0.8f else 0.55f),
                )
                .clip(thumbShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            thumbAccentColor.copy(alpha = 0.95f),
                            thumbAccentColor,
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    color = Color.White.copy(alpha = 0.45f),
                    shape = thumbShape,
                )
                .clickable {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    when (state) {
                        ConnectionState.DISCONNECTED -> {
                            coroutineScope.launch {
                                if (maxDragPx > 0f) {
                                    offsetX.animateTo(maxDragPx, spring(stiffness = 400f))
                                }
                                onRequestConnect()
                                kotlinx.coroutines.delay(350)
                                if (state == ConnectionState.DISCONNECTED) {
                                    offsetX.animateTo(0f, spring(stiffness = 400f))
                                }
                            }
                        }
                        ConnectionState.CONNECTED -> {
                            coroutineScope.launch {
                                isDisconnecting = true
                                offsetX.animateTo(0f, spring(stiffness = 400f))
                                onDisconnect()
                                kotlinx.coroutines.delay(400)
                                isDisconnecting = false
                            }
                        }
                        ConnectionState.CONNECTING -> {
                            coroutineScope.launch {
                                offsetX.animateTo(0f, spring(stiffness = 400f))
                                onDisconnect()
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            when {
                isDisconnecting || state == ConnectionState.CONNECTING -> {
                    CircularProgressIndicator(
                        strokeWidth = 2.5.dp,
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                    )
                }
                state == ConnectionState.DISCONNECTED -> {
                    Icon(
                        imageVector = Icons.Filled.PowerSettingsNew,
                        contentDescription = "Slide to connect",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                }
                state == ConnectionState.CONNECTED -> {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Connected",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}
