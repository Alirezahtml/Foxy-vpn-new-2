package com.vauth.foxyvpn.ui.components

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BlurMaskFilter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vauth.foxyvpn.data.DynamicIslandLedStyle
import com.vauth.foxyvpn.data.DynamicIslandMotionStyle
import com.vauth.foxyvpn.data.SettingsStore
import com.vauth.foxyvpn.data.model.ConnectionState
import com.vauth.foxyvpn.data.model.ProxyCandidate
import com.vauth.foxyvpn.data.model.emojiFlag
import com.vauth.foxyvpn.ui.theme.AnimatedSignalTowerWave
import com.vauth.foxyvpn.ui.theme.LocalFoxyStatusColors
import com.vauth.foxyvpn.ui.theme.automotiveLedBorder
import com.vauth.foxyvpn.ui.theme.currentFoxyFonts
import com.vauth.foxyvpn.ui.theme.currentFoxyStrings
import com.vauth.foxyvpn.ui.theme.currentNeumorphicColors
import com.vauth.foxyvpn.ui.theme.dynamicIslandMotionEffect
import com.vauth.foxyvpn.ui.theme.formatBytesLocalized
import com.vauth.foxyvpn.ui.theme.formatDurationLocalized
import com.vauth.foxyvpn.ui.theme.formatPercentLocalized
import com.vauth.foxyvpn.ui.theme.formatPingLocalized
import com.vauth.foxyvpn.ui.theme.formatSpeedLocalized
import com.vauth.foxyvpn.ui.theme.localizeCountryName
import com.vauth.foxyvpn.ui.theme.localizeNumber
import com.vauth.foxyvpn.ui.theme.neumorphicTouchBounce
import com.vauth.foxyvpn.vpn.FoxyVpnService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

/**
 * Native OS Drop-Shadow & Ambient Glow Modifier for Dynamic Island.
 */
private fun Modifier.nativeIslandDropShadow(
    cornerRadius: Dp,
    shadowElevation: Dp,
    glowColor: Color,
    glowAlpha: Float,
): Modifier = this.then(
    Modifier.drawBehind {
        if (size.width <= 4f || size.height <= 4f) return@drawBehind
        val rPx = cornerRadius.toPx().coerceAtLeast(0f)
        val blurPx = shadowElevation.toPx().coerceAtLeast(1f)
        val dyPx = (shadowElevation * 0.42f).toPx()

        drawIntoCanvas { canvas ->
            // 1. Soft OS Drop Shadow
            val dropPaint = Paint().asFrameworkPaint().apply {
                color = Color.Black.copy(alpha = 0.48f).toArgb()
                isAntiAlias = true
                maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
            }
            canvas.nativeCanvas.drawRoundRect(
                2f,
                dyPx,
                size.width - 2f,
                size.height + dyPx * 0.8f,
                rPx,
                rPx,
                dropPaint,
            )

            // 2. Subtle Ambient Status Glow
            if (glowAlpha > 0.02f) {
                val glowPaint = Paint().asFrameworkPaint().apply {
                    color = glowColor.copy(alpha = glowAlpha.coerceIn(0f, 0.55f)).toArgb()
                    isAntiAlias = true
                    maskFilter = BlurMaskFilter(blurPx * 1.25f, BlurMaskFilter.Blur.NORMAL)
                }
                canvas.nativeCanvas.drawRoundRect(
                    0f,
                    0f,
                    size.width,
                    size.height,
                    rPx,
                    rPx,
                    glowPaint,
                )
            }
        }
    }
)

data class IslandPinnedAppInfo(
    val packageName: String,
    val label: String,
)

@Composable
fun DynamicIsland(
    settingsStore: SettingsStore,
    selectedProxy: ProxyCandidate?,
    onDisconnect: () -> Unit,
    onRequestConnect: () -> Unit,
    onOpenServers: () -> Unit,
    isSystemOverlay: Boolean = false,
    onOverlayDragDelta: ((Float, Float) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val configRevision by settingsStore.islandConfigRevision.collectAsState()
    @Suppress("UNUSED_EXPRESSION")
    configRevision

    if (!settingsStore.dynamicIslandEnabled) return

    val context = LocalContext.current
    val state by FoxyVpnService.state.collectAsState()
    val liveStats by FoxyVpnService.vpnLiveStats.collectAsState()
    val connectedAtEpochMs by FoxyVpnService.connectedAtEpochMs.collectAsState()
    val pingLatency by FoxyVpnService.pingLatencyMs.collectAsState()

    val neu = currentNeumorphicColors
    val statusColors = LocalFoxyStatusColors.current
    val s = currentFoxyStrings
    val fonts = currentFoxyFonts
    val haptics = LocalHapticFeedback.current

    var isExpanded by remember { mutableStateOf(false) }
    var showCustomizeModal by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableLongStateOf(0L) }

    // Touch-drag position state for in-app dragging (synced with SettingsStore)
    var inAppOffsetX by remember(configRevision) { mutableFloatStateOf(settingsStore.dynamicIslandOffsetX) }
    var inAppOffsetY by remember(configRevision) { mutableFloatStateOf(settingsStore.dynamicIslandOffsetY) }

    LaunchedEffect(state, connectedAtEpochMs) {
        if (state == ConnectionState.CONNECTED && connectedAtEpochMs != null) {
            while (isActive) {
                val now = System.currentTimeMillis()
                elapsedSeconds = ((now - connectedAtEpochMs!!) / 1000L).coerceAtLeast(0L)
                delay(1000L)
            }
        } else {
            elapsedSeconds = 0L
        }
    }

    // Load labels for pinned apps
    val pinnedPacNames = settingsStore.dynamicIslandPinnedApps
    val pinnedApps by produceState<List<IslandPinnedAppInfo>>(initialValue = emptyList(), key1 = pinnedPacNames) {
        value = withContext(Dispatchers.IO) {
            val pm = context.packageManager
            pinnedPacNames.mapNotNull { pkg ->
                runCatching {
                    val info = pm.getApplicationInfo(pkg, 0)
                    IslandPinnedAppInfo(pkg, pm.getApplicationLabel(info).toString())
                }.getOrNull()
            }
        }
    }

    // Soft breathing glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "island-native-glow")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.22f,
        targetValue = 0.58f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow-pulse",
    )

    val islandTheme = settingsStore.dynamicIslandTheme
    val pillBrush = when (islandTheme) {
        SettingsStore.DynamicIslandTheme.DARK -> Brush.verticalGradient(
            colors = listOf(
                Color(0xF212161F),
                Color(0xF5080A0F),
            ),
        )
        SettingsStore.DynamicIslandTheme.ACCENT -> Brush.linearGradient(
            colors = listOf(
                neu.accent.copy(alpha = 0.94f),
                neu.accent.copy(alpha = 0.84f),
            ),
        )
        SettingsStore.DynamicIslandTheme.NEUMORPHIC -> Brush.verticalGradient(
            colors = if (neu.isDark) {
                listOf(Color(0xEB1D222B), Color(0xF0141820))
            } else {
                listOf(Color(0xF2F2F6FC), Color(0xEBDFE6F2))
            },
        )
    }

    val contentColor = when (islandTheme) {
        SettingsStore.DynamicIslandTheme.DARK -> Color.White
        SettingsStore.DynamicIslandTheme.ACCENT -> Color.White
        SettingsStore.DynamicIslandTheme.NEUMORPHIC -> neu.textPrimary
    }

    // Manual 0%..100% linear size scale factor
    val scalePercent = settingsStore.dynamicIslandScalePercent.coerceIn(0, 100)
    val manualScaleFactor by animateFloatAsState(
        targetValue = 0.74f + (scalePercent / 100f) * 0.52f, // 0% -> 0.74x, 50% -> 1.00x, 100% -> 1.26x
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "island-manual-scale",
    )

    val sizeConfig = settingsStore.dynamicIslandSize
    val scaledMinWidth = (sizeConfig.widthDp * manualScaleFactor).dp
    val scaledMaxCollapsedWidth = ((sizeConfig.widthDp + 110) * manualScaleFactor).coerceAtMost(430f).dp
    val scaledMinHeight = (sizeConfig.heightDp * manualScaleFactor).dp

    val baseCornerRadius = settingsStore.dynamicIslandCornerRadius.dp
    val pillCornerRadius by animateDpAsState(
        targetValue = if (isExpanded) 30.dp else (baseCornerRadius * manualScaleFactor).coerceIn(18.dp, 42.dp),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "island-corner-radius",
    )

    val activeStatusColor by animateColorAsState(
        targetValue = when (state) {
            ConnectionState.CONNECTED -> statusColors.connected
            ConnectionState.CONNECTING -> statusColors.connecting
            ConnectionState.DISCONNECTED -> neu.accent
        },
        animationSpec = tween(350),
        label = "island-status-color",
    )

    val alignment = when (settingsStore.dynamicIslandPosition) {
        SettingsStore.DynamicIslandPosition.TOP_START -> Alignment.TopStart
        SettingsStore.DynamicIslandPosition.TOP_END -> Alignment.TopEnd
        SettingsStore.DynamicIslandPosition.TOP_CENTER -> Alignment.TopCenter
    }

    if (showCustomizeModal && !isSystemOverlay) {
        DynamicIslandCustomizationModal(
            settingsStore = settingsStore,
            onDismiss = { showCustomizeModal = false },
        )
    }

    val isDraggable = settingsStore.dynamicIslandDraggable
    val dragGestureModifier = if (isDraggable) {
        Modifier.pointerInput(isSystemOverlay, isDraggable) {
            detectDragGestures(
                onDragEnd = {
                    if (!isSystemOverlay) {
                        settingsStore.dynamicIslandOffsetX = inAppOffsetX
                        settingsStore.dynamicIslandOffsetY = inAppOffsetY
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    if (isSystemOverlay && onOverlayDragDelta != null) {
                        onOverlayDragDelta(dragAmount.x, dragAmount.y)
                    } else {
                        inAppOffsetX = (inAppOffsetX + dragAmount.x).coerceIn(-360f, 360f)
                        inAppOffsetY = (inAppOffsetY + dragAmount.y).coerceIn(-60f, 900f)
                    }
                },
            )
        }
    } else {
        Modifier
    }

    // Generous outer padding prevents any shadow/glow/LED clipping on system overlays
    Box(
        modifier = modifier
            .then(if (isSystemOverlay) Modifier else Modifier.fillMaxWidth())
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = alignment,
    ) {
        val shape = RoundedCornerShape(pillCornerRadius)
        val effectiveGlowAlpha = when {
            !settingsStore.dynamicIslandAnimation -> 0.12f
            state == ConnectionState.CONNECTED || state == ConnectionState.CONNECTING -> glowPulse
            else -> 0.10f
        }

        val smoothOffsetX by animateFloatAsState(
            targetValue = inAppOffsetX,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
            label = "island-smooth-drag-x",
        )
        val smoothOffsetY by animateFloatAsState(
            targetValue = inAppOffsetY,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
            label = "island-smooth-drag-y",
        )

        Box(
            modifier = Modifier
                .then(
                    if (!isSystemOverlay) {
                        Modifier.graphicsLayer {
                            translationX = smoothOffsetX
                            translationY = smoothOffsetY
                        }
                    } else {
                        Modifier
                    }
                )
                .then(dragGestureModifier)
                .widthIn(
                    min = scaledMinWidth,
                    max = if (isExpanded) 440.dp else scaledMaxCollapsedWidth,
                )
                .heightIn(min = if (isExpanded) 164.dp else scaledMinHeight)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = 0.78f,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                )
                .nativeIslandDropShadow(
                    cornerRadius = pillCornerRadius,
                    shadowElevation = if (isExpanded) 12.dp else 7.dp,
                    glowColor = activeStatusColor,
                    glowAlpha = effectiveGlowAlpha,
                )
                .dynamicIslandMotionEffect(
                    enabled = settingsStore.dynamicIslandAnimation && !settingsStore.batterySaverModeEnabled,
                    style = settingsStore.dynamicIslandMotionStyle,
                    isConnected = state == ConnectionState.CONNECTED,
                    isExpanded = isExpanded,
                    trafficRateBytesPerSec = liveStats.rxRateBytesPerSec + liveStats.txRateBytesPerSec,
                    accentColor = activeStatusColor,
                    cornerRadius = pillCornerRadius,
                )
                .clip(shape)
                .background(pillBrush)
                .automotiveLedBorder(
                    enabled = settingsStore.dynamicIslandLedBorderEnabled,
                    style = settingsStore.dynamicIslandLedStyle,
                    speedMultiplier = if (settingsStore.batterySaverModeEnabled) {
                        (settingsStore.dynamicIslandLedSpeed * 0.65f).coerceAtLeast(0.35f)
                    } else {
                        settingsStore.dynamicIslandLedSpeed
                    },
                    cornerRadius = pillCornerRadius,
                    borderWidth = if (isExpanded) 2.6.dp else 2.2.dp,
                    glowRadius = if (isExpanded) 10.dp else 7.dp,
                )
                .border(
                    width = if (settingsStore.dynamicIslandLedBorderEnabled) 0.dp else 1.dp,
                    brush = Brush.verticalGradient(
                        colors = when (state) {
                            ConnectionState.CONNECTED -> listOf(
                                statusColors.connected.copy(alpha = 0.52f),
                                statusColors.connected.copy(alpha = 0.16f),
                            )
                            ConnectionState.CONNECTING -> listOf(
                                statusColors.connecting.copy(alpha = 0.52f),
                                statusColors.connecting.copy(alpha = 0.16f),
                            )
                            ConnectionState.DISCONNECTED -> listOf(
                                Color.White.copy(alpha = if (islandTheme == SettingsStore.DynamicIslandTheme.NEUMORPHIC && !neu.isDark) 0.85f else 0.18f),
                                Color.White.copy(alpha = 0.04f),
                            )
                        },
                    ),
                    shape = shape,
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    isExpanded = !isExpanded
                }
                .padding(
                    horizontal = if (isExpanded) 20.dp else (16.dp * manualScaleFactor).coerceIn(12.dp, 22.dp),
                    vertical = if (isExpanded) 18.dp else (9.dp * manualScaleFactor).coerceIn(7.dp, 14.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedContent(
                targetState = isExpanded,
                transitionSpec = {
                    (fadeIn(tween(240, easing = FastOutSlowInEasing)) +
                        scaleIn(
                            initialScale = 0.92f,
                            animationSpec = spring(
                                dampingRatio = 0.76f,
                                stiffness = Spring.StiffnessMediumLow,
                            ),
                        ))
                        .togetherWith(
                            fadeOut(tween(160)) +
                                scaleOut(
                                    targetScale = 0.94f,
                                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                                )
                        )
                },
                label = "island-expand-collapse",
            ) { expanded ->
                if (!expanded) {
                    // Collapsed Native Dynamic Island Pill with Signal Tower Wave & Customizable Telemetry
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        when (state) {
                            ConnectionState.CONNECTED -> {
                                if (settingsStore.dynamicIslandShowTowerWave) {
                                    AnimatedSignalTowerWave(
                                        active = settingsStore.dynamicIslandAnimation,
                                        color = statusColors.connected,
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(statusColors.connected),
                                    )
                                }
                            }
                            ConnectionState.CONNECTING -> {
                                if (settingsStore.dynamicIslandShowTowerWave) {
                                    AnimatedSignalTowerWave(
                                        active = true,
                                        color = statusColors.connecting,
                                    )
                                } else {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(12.dp),
                                        color = statusColors.connecting,
                                    )
                                }
                            }
                            ConnectionState.DISCONNECTED -> {
                                if (settingsStore.dynamicIslandShowTowerWave) {
                                    AnimatedSignalTowerWave(
                                        active = false,
                                        color = contentColor.copy(alpha = 0.65f),
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(contentColor.copy(alpha = 0.45f)),
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.width(8.dp))

                        when (state) {
                            ConnectionState.CONNECTED -> {
                                val flag = selectedProxy?.emojiFlag
                                if (settingsStore.dynamicIslandShowFlag && !flag.isNullOrBlank()) {
                                    Text(flag, fontSize = (13f * manualScaleFactor.coerceIn(0.85f, 1.15f)).sp)
                                    Spacer(Modifier.width(6.dp))
                                }
                                if (settingsStore.dynamicIslandShowServerName && selectedProxy != null) {
                                    Text(
                                        text = s.language.localizeCountryName(selectedProxy.countryName, selectedProxy.countryCode),
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontFamily = fonts.headingFont,
                                            fontWeight = FontWeight.Bold,
                                        ),
                                        color = contentColor,
                                        maxLines = 1,
                                    )
                                    Spacer(Modifier.width(6.dp))
                                }
                                if (settingsStore.dynamicIslandShowTimer) {
                                    Text(
                                        text = s.language.formatDurationLocalized(elapsedSeconds, alwaysShowHours = false),
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontFamily = fonts.monoFont,
                                            fontWeight = FontWeight.Bold,
                                            fontFeatureSettings = "tnum",
                                        ),
                                        color = contentColor,
                                        maxLines = 1,
                                    )
                                } else if (!settingsStore.dynamicIslandShowServerName) {
                                    Text(
                                        text = s.appName,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontFamily = fonts.headingFont,
                                            fontWeight = FontWeight.Bold,
                                        ),
                                        color = contentColor,
                                        maxLines = 1,
                                    )
                                }

                                if (settingsStore.dynamicIslandShowSpeed) {
                                    Spacer(Modifier.width(8.dp))
                                    AnimatedContent(
                                        targetState = s.language.formatSpeedLocalized(liveStats.rxRateBytesPerSec),
                                        transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(120)) },
                                        label = "island-live-speed",
                                    ) { rxFormatted ->
                                        Text(
                                            text = "↓$rxFormatted",
                                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.monoFont),
                                            color = statusColors.connected,
                                            maxLines = 1,
                                        )
                                    }
                                }
                                if (settingsStore.dynamicIslandShowPing && pingLatency != null) {
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = s.language.formatPingLocalized(pingLatency!!),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontFamily = fonts.monoFont,
                                            fontWeight = FontWeight.SemiBold,
                                        ),
                                        color = contentColor.copy(alpha = 0.85f),
                                        maxLines = 1,
                                    )
                                }
                                if (settingsStore.dynamicIslandShowSessionData) {
                                    val totalBytes = liveStats.totalRxBytes + liveStats.totalTxBytes
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = s.language.formatBytesLocalized(totalBytes),
                                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.monoFont),
                                        color = contentColor.copy(alpha = 0.78f),
                                        maxLines = 1,
                                    )
                                }
                            }
                            ConnectionState.CONNECTING -> {
                                Text(
                                    text = s.islandConnecting,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontFamily = fonts.bodyFont,
                                        fontWeight = FontWeight.SemiBold,
                                    ),
                                    color = statusColors.connecting,
                                    maxLines = 1,
                                )
                            }
                            ConnectionState.DISCONNECTED -> {
                                Text(
                                    text = s.appName,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontFamily = fonts.headingFont,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    color = contentColor.copy(alpha = 0.92f),
                                    maxLines = 1,
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = s.islandReady,
                                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.bodyFont),
                                    color = contentColor.copy(alpha = 0.65f),
                                    maxLines = 1,
                                )
                            }
                        }

                        Spacer(Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = contentColor.copy(alpha = 0.55f),
                            modifier = Modifier.size(14.dp),
                        )
                    }
                } else {
                    // Expanded Native Dynamic Island Sheet
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        // Header Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f),
                            ) {
                                if (settingsStore.dynamicIslandShowTowerWave) {
                                    AnimatedSignalTowerWave(
                                        active = state == ConnectionState.CONNECTED || state == ConnectionState.CONNECTING,
                                        color = activeStatusColor,
                                    )
                                    Spacer(Modifier.width(8.dp))
                                }
                                val flag = selectedProxy?.emojiFlag
                                if (settingsStore.dynamicIslandShowFlag && !flag.isNullOrBlank()) {
                                    Text(flag, fontSize = 20.sp)
                                    Spacer(Modifier.width(8.dp))
                                } else if (!settingsStore.dynamicIslandShowTowerWave) {
                                    Icon(
                                        imageVector = Icons.Filled.Public,
                                        contentDescription = null,
                                        tint = contentColor,
                                        modifier = Modifier.size(20.dp),
                                    )
                                    Spacer(Modifier.width(8.dp))
                                }
                                Column {
                                    Text(
                                        text = selectedProxy?.let {
                                            s.language.localizeCountryName(it.countryName, it.countryCode)
                                        } ?: s.islandServerDefault,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontFamily = fonts.headingFont,
                                            fontWeight = FontWeight.Bold,
                                        ),
                                        color = contentColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = when (state) {
                                                ConnectionState.CONNECTED -> s.islandActiveEncrypted
                                                ConnectionState.CONNECTING -> s.islandSecuringConnection
                                                ConnectionState.DISCONNECTED -> s.islandProtectedReady
                                            },
                                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                                            color = when (state) {
                                                ConnectionState.CONNECTED -> statusColors.connected
                                                ConnectionState.CONNECTING -> statusColors.connecting
                                                ConnectionState.DISCONNECTED -> contentColor.copy(alpha = 0.65f)
                                            },
                                            maxLines = 1,
                                        )
                                        if (settingsStore.dynamicIslandShowIpBadge && selectedProxy != null) {
                                            Spacer(Modifier.width(6.dp))
                                            Text(
                                                text = "• H2/TLS :${s.language.localizeNumber(selectedProxy.port)}",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontFamily = fonts.monoFont,
                                                    fontWeight = FontWeight.Bold,
                                                ),
                                                color = neu.accent,
                                                maxLines = 1,
                                            )
                                        }
                                    }
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (!isSystemOverlay) {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape)
                                            .background(contentColor.copy(alpha = 0.12f))
                                            .clickable { showCustomizeModal = true },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Tune,
                                            contentDescription = s.islandCustomizeButton,
                                            tint = contentColor.copy(alpha = 0.85f),
                                            modifier = Modifier.size(16.dp),
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                }
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(contentColor.copy(alpha = 0.10f))
                                        .clickable { isExpanded = false },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.KeyboardArrowUp,
                                        contentDescription = null,
                                        tint = contentColor.copy(alpha = 0.8f),
                                        modifier = Modifier.size(18.dp),
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Telemetry Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            if (settingsStore.dynamicIslandShowPing) {
                                Column(modifier = Modifier.weight(0.9f)) {
                                    Text(
                                        text = s.islandLatency,
                                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.bodyFont),
                                        color = contentColor.copy(alpha = 0.62f),
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = pingLatency?.let { s.language.formatPingLocalized(it) } ?: "\u2014",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = fonts.monoFont,
                                            fontWeight = FontWeight.Bold,
                                        ),
                                        color = if (pingLatency != null) statusColors.connected else contentColor,
                                    )
                                }
                            }

                            if (settingsStore.dynamicIslandShowTimer) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = s.islandDuration,
                                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.bodyFont),
                                        color = contentColor.copy(alpha = 0.62f),
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = if (state == ConnectionState.CONNECTED) {
                                            s.language.formatDurationLocalized(elapsedSeconds, alwaysShowHours = true)
                                        } else {
                                            s.language.formatDurationLocalized(0L, alwaysShowHours = true)
                                        },
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = fonts.monoFont,
                                            fontWeight = FontWeight.Bold,
                                            fontFeatureSettings = "tnum",
                                        ),
                                        color = contentColor,
                                    )
                                }
                            }

                            if (settingsStore.dynamicIslandShowSessionData) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = s.dataUsedLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.bodyFont),
                                        color = contentColor.copy(alpha = 0.62f),
                                        maxLines = 1,
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = s.language.formatBytesLocalized(liveStats.totalRxBytes + liveStats.totalTxBytes),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = fonts.monoFont,
                                            fontWeight = FontWeight.Bold,
                                        ),
                                        color = contentColor,
                                        maxLines = 1,
                                    )
                                }
                            }

                            if (settingsStore.dynamicIslandShowSpeed) {
                                Column(modifier = Modifier.weight(1.35f)) {
                                    Text(
                                        text = s.islandLiveSpeed,
                                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.bodyFont),
                                        color = contentColor.copy(alpha = 0.62f),
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    val tx = s.language.formatSpeedLocalized(liveStats.txRateBytesPerSec)
                                    val rx = s.language.formatSpeedLocalized(liveStats.rxRateBytesPerSec)
                                    Text(
                                        text = "↑$tx  ↓$rx",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontFamily = fonts.monoFont,
                                            fontWeight = FontWeight.SemiBold,
                                        ),
                                        color = contentColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }

                        // Pinned Quick-Launch Apps Row
                        AnimatedVisibility(
                            visible = pinnedApps.isNotEmpty() || !isSystemOverlay,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically(),
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Spacer(Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    pinnedApps.forEach { appItem ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(contentColor.copy(alpha = 0.12f))
                                                .clickable {
                                                    runCatching {
                                                        val launchIntent = context.packageManager.getLaunchIntentForPackage(appItem.packageName)
                                                        if (launchIntent != null) {
                                                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                            context.startActivity(launchIntent)
                                                            isExpanded = false
                                                        }
                                                    }
                                                }
                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Filled.Apps,
                                                    contentDescription = null,
                                                    tint = neu.accent,
                                                    modifier = Modifier.size(14.dp),
                                                )
                                                Spacer(Modifier.width(6.dp))
                                                Text(
                                                    text = appItem.label,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontFamily = fonts.bodyFont,
                                                        fontWeight = FontWeight.SemiBold,
                                                    ),
                                                    color = contentColor,
                                                    maxLines = 1,
                                                )
                                            }
                                        }
                                    }

                                    if (!isSystemOverlay) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(neu.accent.copy(alpha = 0.18f))
                                                .clickable { showCustomizeModal = true }
                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Filled.Add,
                                                    contentDescription = s.islandCustomizeButton,
                                                    tint = contentColor,
                                                    modifier = Modifier.size(14.dp),
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text(
                                                    text = s.islandCustomizeButton,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontFamily = fonts.bodyFont,
                                                        fontWeight = FontWeight.Bold,
                                                    ),
                                                    color = contentColor,
                                                    maxLines = 1,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Primary Actions Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .neumorphicTouchBounce()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(contentColor.copy(alpha = 0.13f))
                                    .clickable {
                                        isExpanded = false
                                        onOpenServers()
                                    }
                                    .padding(vertical = 11.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = s.islandChangeServer,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontFamily = fonts.bodyFont,
                                        fontWeight = FontWeight.SemiBold,
                                    ),
                                    color = contentColor,
                                    maxLines = 1,
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .neumorphicTouchBounce()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (state == ConnectionState.CONNECTED) statusColors.connected.copy(alpha = 0.24f)
                                        else neu.accent.copy(alpha = 0.28f),
                                    )
                                    .clickable {
                                        isExpanded = false
                                        if (state == ConnectionState.DISCONNECTED) onRequestConnect() else onDisconnect()
                                    }
                                    .padding(vertical = 11.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = when (state) {
                                        ConnectionState.CONNECTED -> s.islandDisconnect
                                        ConnectionState.CONNECTING -> s.islandCancel
                                        ConnectionState.DISCONNECTED -> s.islandConnect
                                    },
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontFamily = fonts.headingFont,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    color = if (state == ConnectionState.CONNECTED) statusColors.connected else Color.White,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Full Dynamic Island Customization Studio Modal (accessible from Dynamic Island & Settings).
 * Includes:
 * 1. Linear manual size slider from 0% to 100%
 * 2. Automotive RGB Chasing LED Border toggle, 5 color style chips, and speed slider
 * 3. Touch-Drag repositioning toggle & Reset Position action
 * 4. Granular content visibility switches (Signal Tower Wave, Timer, Live Speed, Ping, Flag, Server Name, Session Data, Protocol Badge)
 * 5. App shortcut selector to add/remove specific apps inside the Dynamic Island
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DynamicIslandCustomizationModal(
    settingsStore: SettingsStore,
    onDismiss: () -> Unit,
) {
    val s = currentFoxyStrings
    val neu = currentNeumorphicColors
    val fonts = currentFoxyFonts

    var scalePercent by remember { mutableFloatStateOf(settingsStore.dynamicIslandScalePercent.toFloat()) }
    var ledEnabled by remember { mutableStateOf(settingsStore.dynamicIslandLedBorderEnabled) }
    var ledStyle by remember { mutableStateOf(settingsStore.dynamicIslandLedStyle) }
    var motionStyle by remember { mutableStateOf(settingsStore.dynamicIslandMotionStyle) }
    var ledSpeed by remember { mutableFloatStateOf(settingsStore.dynamicIslandLedSpeed) }
    var draggable by remember { mutableStateOf(settingsStore.dynamicIslandDraggable) }

    var showTowerWave by remember { mutableStateOf(settingsStore.dynamicIslandShowTowerWave) }
    var showTimer by remember { mutableStateOf(settingsStore.dynamicIslandShowTimer) }
    var showSpeed by remember { mutableStateOf(settingsStore.dynamicIslandShowSpeed) }
    var showPing by remember { mutableStateOf(settingsStore.dynamicIslandShowPing) }
    var showFlag by remember { mutableStateOf(settingsStore.dynamicIslandShowFlag) }
    var showServerName by remember { mutableStateOf(settingsStore.dynamicIslandShowServerName) }
    var showSessionData by remember { mutableStateOf(settingsStore.dynamicIslandShowSessionData) }
    var showIpBadge by remember { mutableStateOf(settingsStore.dynamicIslandShowIpBadge) }
    var showAnim by remember { mutableStateOf(settingsStore.dynamicIslandAnimation) }
    var pinnedApps by remember { mutableStateOf(settingsStore.dynamicIslandPinnedApps) }
    var showAppPicker by remember { mutableStateOf(false) }

    if (showAppPicker) {
        DynamicIslandAppPickerDialog(
            current = pinnedApps,
            onDismiss = { showAppPicker = false },
            onConfirm = { newSet ->
                pinnedApps = newSet
                settingsStore.dynamicIslandPinnedApps = newSet
                showAppPicker = false
            },
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = s.islandCustomizeModalTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = fonts.headingFont,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = s.islandCustomizeModalSub,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                // 1. Linear Manual Scale Slider (0% - 100%)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = s.islandManualScaleTitle,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = fonts.headingFont,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = neu.textPrimary,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = s.language.formatPercentLocalized(scalePercent.roundToInt()),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = fonts.monoFont,
                            fontWeight = FontWeight.ExtraBold,
                        ),
                        color = neu.accent,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = s.islandManualScaleSub,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                    color = neu.textSecondary,
                )
                Spacer(Modifier.height(6.dp))
                Slider(
                    value = scalePercent,
                    onValueChange = { newVal ->
                        scalePercent = newVal
                        settingsStore.dynamicIslandScalePercent = newVal.roundToInt()
                    },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = neu.accent,
                        activeTrackColor = neu.accent,
                    ),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = s.language.formatPercentLocalized(0),
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.monoFont),
                        color = neu.textSecondary,
                    )
                    Text(
                        text = s.language.formatPercentLocalized(50),
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.monoFont),
                        color = neu.textSecondary,
                    )
                    Text(
                        text = s.language.formatPercentLocalized(100),
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.monoFont),
                        color = neu.textSecondary,
                    )
                }

                HorizontalDivider(
                    color = neu.textSecondary.copy(alpha = 0.14f),
                    modifier = Modifier.padding(vertical = 14.dp),
                )

                // 2. Automotive RGB Chasing LED Border Controls
                IslandModalSwitchRow(
                    title = s.islandLedBorderTitle,
                    checked = ledEnabled,
                    onCheckedChange = {
                        ledEnabled = it
                        settingsStore.dynamicIslandLedBorderEnabled = it
                    },
                )
                Text(
                    text = s.islandLedBorderSub,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                    color = neu.textSecondary,
                )
                if (ledEnabled) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = s.islandLedStyleTitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = fonts.headingFont,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = neu.textSecondary,
                    )
                    Spacer(Modifier.height(6.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        DynamicIslandLedStyle.entries.forEach { styleItem ->
                            val selected = ledStyle == styleItem
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (selected) neu.accent else neu.accent.copy(alpha = 0.12f))
                                    .clickable {
                                        ledStyle = styleItem
                                        settingsStore.dynamicIslandLedStyle = styleItem
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                            ) {
                                Text(
                                    text = styleItem.localizedLabel(s.language),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = fonts.headingFont,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    color = if (selected) Color.White else neu.textPrimary,
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = s.islandLedSpeedTitle,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = neu.textPrimary,
                        )
                        Text(
                            text = "${s.language.formatPercentLocalized((ledSpeed * 100).roundToInt())}",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = fonts.monoFont),
                            color = neu.accent,
                        )
                    }
                    Slider(
                        value = ledSpeed,
                        onValueChange = {
                            ledSpeed = it
                            settingsStore.dynamicIslandLedSpeed = it
                        },
                        valueRange = 0.4f..2.2f,
                        colors = SliderDefaults.colors(
                            thumbColor = neu.accent,
                            activeTrackColor = neu.accent,
                        ),
                    )
                }

                HorizontalDivider(
                    color = neu.textSecondary.copy(alpha = 0.14f),
                    modifier = Modifier.padding(vertical = 14.dp),
                )

                // 3. Touch-Drag Free Repositioning & Reset Position
                IslandModalSwitchRow(
                    title = s.islandDraggableTitle,
                    checked = draggable,
                    onCheckedChange = {
                        draggable = it
                        settingsStore.dynamicIslandDraggable = it
                    },
                )
                Text(
                    text = s.islandDraggableSub,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                    color = neu.textSecondary,
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(neu.accent.copy(alpha = 0.12f))
                        .clickable {
                            settingsStore.resetDynamicIslandPosition()
                        }
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.RestartAlt,
                            contentDescription = null,
                            tint = neu.accent,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = s.islandResetPositionButton,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = fonts.headingFont,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = neu.accent,
                        )
                    }
                }

                HorizontalDivider(
                    color = neu.textSecondary.copy(alpha = 0.14f),
                    modifier = Modifier.padding(vertical = 14.dp),
                )

                // 4. Pinned Quick-Launch Apps
                Text(
                    text = s.islandPinnedAppsTitle,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = fonts.headingFont,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = neu.textPrimary,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = s.islandPinnedAppsSub,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                    color = neu.textSecondary,
                )
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(neu.accent.copy(alpha = 0.14f))
                        .clickable { showAppPicker = true }
                        .padding(vertical = 12.dp, horizontal = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Apps,
                            contentDescription = null,
                            tint = neu.accent,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "${s.islandAddAppsButton} (${s.language.formatPercentLocalized(pinnedApps.size).replace("٪", "").replace("%", "")})",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = fonts.headingFont,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = neu.accent,
                        )
                    }
                }

                HorizontalDivider(
                    color = neu.textSecondary.copy(alpha = 0.14f),
                    modifier = Modifier.padding(vertical = 14.dp),
                )

                // 5. Granular Displayed Content Toggles
                Text(
                    text = s.islandContentSectionTitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = fonts.headingFont,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = neu.textSecondary,
                )
                Spacer(Modifier.height(8.dp))

                IslandModalSwitchRow(
                    title = s.islandShowTowerWaveTitle,
                    checked = showTowerWave,
                    onCheckedChange = {
                        showTowerWave = it
                        settingsStore.dynamicIslandShowTowerWave = it
                    },
                )
                IslandModalSwitchRow(
                    title = s.islandShowTimerTitle,
                    checked = showTimer,
                    onCheckedChange = {
                        showTimer = it
                        settingsStore.dynamicIslandShowTimer = it
                    },
                )
                IslandModalSwitchRow(
                    title = s.islandShowSpeedTitle,
                    checked = showSpeed,
                    onCheckedChange = {
                        showSpeed = it
                        settingsStore.dynamicIslandShowSpeed = it
                    },
                )
                IslandModalSwitchRow(
                    title = s.islandShowPingTitle,
                    checked = showPing,
                    onCheckedChange = {
                        showPing = it
                        settingsStore.dynamicIslandShowPing = it
                    },
                )
                IslandModalSwitchRow(
                    title = s.islandShowFlagTitle,
                    checked = showFlag,
                    onCheckedChange = {
                        showFlag = it
                        settingsStore.dynamicIslandShowFlag = it
                    },
                )
                IslandModalSwitchRow(
                    title = s.islandShowServerNameTitle,
                    checked = showServerName,
                    onCheckedChange = {
                        showServerName = it
                        settingsStore.dynamicIslandShowServerName = it
                    },
                )
                IslandModalSwitchRow(
                    title = s.islandShowSessionDataTitle,
                    checked = showSessionData,
                    onCheckedChange = {
                        showSessionData = it
                        settingsStore.dynamicIslandShowSessionData = it
                    },
                )
                IslandModalSwitchRow(
                    title = s.islandShowIpBadgeTitle,
                    checked = showIpBadge,
                    onCheckedChange = {
                        showIpBadge = it
                        settingsStore.dynamicIslandShowIpBadge = it
                    },
                )
                IslandModalSwitchRow(
                    title = s.islandAnimationTitle,
                    checked = showAnim,
                    onCheckedChange = {
                        showAnim = it
                        settingsStore.dynamicIslandAnimation = it
                    },
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(s.save, fontWeight = FontWeight.Bold)
            }
        },
    )
}

@Composable
private fun IslandModalSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val neu = currentNeumorphicColors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = neu.textPrimary,
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = neu.accent,
            ),
        )
    }
}

@Composable
fun DynamicIslandAppPickerDialog(
    current: Set<String>,
    onDismiss: () -> Unit,
    onConfirm: (Set<String>) -> Unit,
) {
    val context = LocalContext.current
    val s = currentFoxyStrings
    var selected by remember { mutableStateOf(current) }
    var searchQuery by remember { mutableStateOf("") }

    val installedApps by produceState<List<IslandPinnedAppInfo>>(initialValue = emptyList()) {
        value = withContext(Dispatchers.IO) {
            val pm = context.packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            packages
                .filter {
                    (it.flags and ApplicationInfo.FLAG_SYSTEM == 0 || it.packageName == "com.android.chrome") &&
                        pm.getLaunchIntentForPackage(it.packageName) != null
                }
                .map { IslandPinnedAppInfo(it.packageName, pm.getApplicationLabel(it).toString()) }
                .sortedBy { it.label.lowercase() }
        }
    }

    val filteredApps = remember(installedApps, searchQuery) {
        if (searchQuery.isBlank()) installedApps
        else installedApps.filter {
            it.label.contains(searchQuery, ignoreCase = true) ||
                it.packageName.contains(searchQuery, ignoreCase = true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.islandPinnedAppsTitle) },
        text = {
            Column(Modifier.height(380.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(s.dialogSearchAppsPlaceholder) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )
                if (installedApps.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(Modifier.weight(1f)) {
                        items(filteredApps, key = { it.packageName }) { app ->
                            val isChecked = selected.contains(app.packageName)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selected = if (isChecked) selected - app.packageName
                                        else selected + app.packageName
                                    }
                                    .padding(vertical = 4.dp),
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { check ->
                                        selected = if (check) selected + app.packageName
                                        else selected - app.packageName
                                    },
                                )
                                Column(Modifier.padding(start = 8.dp)) {
                                    Text(app.label, maxLines = 1, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        app.packageName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) {
                Text(s.save)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(s.cancel)
            }
        },
    )
}
