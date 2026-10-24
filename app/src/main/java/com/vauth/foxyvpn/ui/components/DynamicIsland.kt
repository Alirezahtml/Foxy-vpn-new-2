package com.vauth.foxyvpn.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vauth.foxyvpn.data.DynamicIslandLedStyle
import com.vauth.foxyvpn.data.DynamicIslandMotionStyle
import com.vauth.foxyvpn.data.SettingsStore
import com.vauth.foxyvpn.data.model.ConnectionState
import com.vauth.foxyvpn.data.model.ProxyCandidate
import com.vauth.foxyvpn.data.model.emojiFlag
import com.vauth.foxyvpn.ui.theme.AnimatedSignalTowerWave
import com.vauth.foxyvpn.ui.theme.AppLanguage
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

data class IslandPinnedAppInfo(
    val packageName: String,
    val label: String,
)

private data class IslandTelemetryCardItem(
    val label: String,
    val value: String,
    val valueColor: Color,
)

private val KNOWN_APP_FALLBACK_LABELS = mapOf(
    "com.google.android.youtube" to "YouTube",
    "org.telegram.messenger" to "Telegram",
    "com.android.chrome" to "Chrome",
    "com.instagram.android" to "Instagram",
    "com.whatsapp" to "WhatsApp",
    "com.twitter.android" to "X (Twitter)",
    "com.spotify.music" to "Spotify",
    "com.discord" to "Discord",
    "com.zhiliaoapp.musically" to "TikTok",
)

private val KNOWN_APP_WEB_FALLBACKS = mapOf(
    "com.google.android.youtube" to "https://m.youtube.com",
    "org.telegram.messenger" to "https://web.telegram.org",
    "com.android.chrome" to "https://www.google.com",
    "com.instagram.android" to "https://www.instagram.com",
    "com.whatsapp" to "https://web.whatsapp.com",
    "com.twitter.android" to "https://x.com",
    "com.spotify.music" to "https://open.spotify.com",
    "com.discord" to "https://discord.com/app",
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
    var showDirectAppPicker by remember { mutableStateOf(false) }
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

    // Load labels for pinned apps (with clean fallback labels for YouTube, Telegram, Chrome, etc.)
    val pinnedPacNames = settingsStore.dynamicIslandPinnedApps
    val pinnedApps by produceState<List<IslandPinnedAppInfo>>(initialValue = emptyList(), key1 = pinnedPacNames) {
        value = withContext(Dispatchers.IO) {
            val pm = context.packageManager
            pinnedPacNames.mapNotNull { pkg ->
                runCatching {
                    val info = pm.getApplicationInfo(pkg, 0)
                    IslandPinnedAppInfo(pkg, pm.getApplicationLabel(info).toString())
                }.getOrElse {
                    KNOWN_APP_FALLBACK_LABELS[pkg]?.let { fallbackLabel ->
                        IslandPinnedAppInfo(pkg, fallbackLabel)
                    }
                }
            }
        }
    }

    val islandTheme = settingsStore.dynamicIslandTheme
    // 100% Opaque, clean gradients so rounded corners never show dark shadow bleed or corner artifacts
    val pillBrush = when (islandTheme) {
        SettingsStore.DynamicIslandTheme.DARK -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF141923),
                Color(0xFF0A0D14),
            ),
        )
        SettingsStore.DynamicIslandTheme.ACCENT -> Brush.linearGradient(
            colors = listOf(
                neu.accent,
                neu.accent.copy(alpha = 0.92f),
            ),
        )
        SettingsStore.DynamicIslandTheme.NEUMORPHIC -> Brush.verticalGradient(
            colors = if (neu.isDark) {
                listOf(Color(0xFF1D222C), Color(0xFF13171F))
            } else {
                listOf(Color(0xFFF6F9FF), Color(0xFFE7EEF8))
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
    val rawManualScaleFactor by animateFloatAsState(
        targetValue = 0.74f + (scalePercent / 100f) * 0.52f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "island-manual-scale",
    )
    val manualScaleFactor = rawManualScaleFactor.takeIf { it.isFinite() }?.coerceIn(0.65f, 1.35f) ?: 1f

    val sizeConfig = settingsStore.dynamicIslandSize
    val minWidthDpVal = (sizeConfig.widthDp * manualScaleFactor).coerceIn(115f, 380f)
    val maxCollapsedWidthDpVal = ((sizeConfig.widthDp + 115) * manualScaleFactor)
        .coerceIn(minWidthDpVal + 16f, 430f)
    val scaledMinWidth = minWidthDpVal.dp
    val scaledMaxCollapsedWidth = maxCollapsedWidthDpVal.dp
    val scaledMinHeight = (sizeConfig.heightDp * manualScaleFactor).coerceIn(30f, 110f).dp

    val baseCornerRadius = settingsStore.dynamicIslandCornerRadius.coerceIn(18, 42).dp
    val rawPillCornerRadius by animateDpAsState(
        targetValue = if (isExpanded) 28.dp else (baseCornerRadius * manualScaleFactor).coerceIn(18.dp, 42.dp),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "island-corner-radius",
    )
    val pillCornerRadius = rawPillCornerRadius.coerceIn(14.dp, 44.dp)

    val activeStatusColor by animateColorAsState(
        targetValue = when (state) {
            ConnectionState.CONNECTED -> statusColors.connected
            ConnectionState.CONNECTING -> statusColors.connecting
            ConnectionState.DISCONNECTED -> neu.accent
        },
        animationSpec = tween(300),
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

    if (showDirectAppPicker && !isSystemOverlay) {
        DynamicIslandAppPickerDialog(
            current = settingsStore.dynamicIslandPinnedApps,
            onDismiss = { showDirectAppPicker = false },
            onConfirm = { updatedSet ->
                settingsStore.dynamicIslandPinnedApps = updatedSet
                showDirectAppPicker = false
            },
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

    Box(
        modifier = modifier
            .then(if (isSystemOverlay) Modifier else Modifier.fillMaxWidth())
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = alignment,
    ) {
        val shape = RoundedCornerShape(pillCornerRadius)

        val smoothOffsetX by animateFloatAsState(
            targetValue = inAppOffsetX.takeIf { it.isFinite() }?.coerceIn(-360f, 360f) ?: 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
            label = "island-smooth-drag-x",
        )
        val smoothOffsetY by animateFloatAsState(
            targetValue = inAppOffsetY.takeIf { it.isFinite() }?.coerceIn(-60f, 900f) ?: 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
            label = "island-smooth-drag-y",
        )

        val effectiveMaxWidth = if (isExpanded) {
            maxOf(460.dp, scaledMinWidth + 24.dp)
        } else {
            maxOf(scaledMaxCollapsedWidth, scaledMinWidth + 8.dp)
        }

        // Clean hardware-accelerated Dynamic Island container:
        // No BlurMaskFilter or duplicate .animateContentSize() -> zero dark corner artifacts and zero expand lag!
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
                    max = effectiveMaxWidth,
                )
                .heightIn(min = scaledMinHeight)
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
                    borderWidth = if (isExpanded) 2.5.dp else 2.1.dp,
                    glowRadius = if (isExpanded) 9.dp else 6.dp,
                )
                .border(
                    width = if (settingsStore.dynamicIslandLedBorderEnabled) 0.dp else 1.2.dp,
                    brush = Brush.verticalGradient(
                        colors = when (state) {
                            ConnectionState.CONNECTED -> listOf(
                                statusColors.connected.copy(alpha = 0.58f),
                                statusColors.connected.copy(alpha = 0.18f),
                            )
                            ConnectionState.CONNECTING -> listOf(
                                statusColors.connecting.copy(alpha = 0.58f),
                                statusColors.connecting.copy(alpha = 0.18f),
                            )
                            ConnectionState.DISCONNECTED -> listOf(
                                Color.White.copy(alpha = if (islandTheme == SettingsStore.DynamicIslandTheme.NEUMORPHIC && !neu.isDark) 0.90f else 0.22f),
                                Color.White.copy(alpha = 0.06f),
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
                    horizontal = if (isExpanded) 18.dp else (16.dp * manualScaleFactor).coerceIn(12.dp, 22.dp),
                    vertical = if (isExpanded) 16.dp else (9.dp * manualScaleFactor).coerceIn(7.dp, 14.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedContent(
                targetState = isExpanded,
                transitionSpec = {
                    (fadeIn(tween(190, easing = FastOutSlowInEasing)) +
                        scaleIn(
                            initialScale = 0.95f,
                            animationSpec = spring(
                                dampingRatio = 0.88f,
                                stiffness = 420f,
                            ),
                        ))
                        .togetherWith(
                            fadeOut(tween(130)) +
                                scaleOut(
                                    targetScale = 0.96f,
                                    animationSpec = spring(
                                        dampingRatio = 0.90f,
                                        stiffness = 460f,
                                    ),
                                )
                        )
                        .using(
                            SizeTransform(clip = true) { _, _ ->
                                spring(
                                    dampingRatio = 0.88f,
                                    stiffness = 410f,
                                )
                            }
                        )
                },
                label = "island-expand-collapse",
            ) { expanded ->
                if (!expanded) {
                    // Collapsed Dynamic Island Pill
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
                                            lineHeight = 20.sp,
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
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontFamily = fonts.bodyFont,
                                                lineHeight = 18.sp,
                                            ),
                                            color = when (state) {
                                                ConnectionState.CONNECTED -> statusColors.connected
                                                ConnectionState.CONNECTING -> statusColors.connecting
                                                ConnectionState.DISCONNECTED -> contentColor.copy(alpha = 0.65f)
                                            },
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
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
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(contentColor.copy(alpha = 0.12f))
                                            .clickable { showCustomizeModal = true },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Tune,
                                            contentDescription = s.islandCustomizeButton,
                                            tint = contentColor.copy(alpha = 0.88f),
                                            modifier = Modifier.size(16.dp),
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                }
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(contentColor.copy(alpha = 0.10f))
                                        .clickable { isExpanded = false },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.KeyboardArrowUp,
                                        contentDescription = null,
                                        tint = contentColor.copy(alpha = 0.85f),
                                        modifier = Modifier.size(18.dp),
                                    )
                                }
                            }
                        }

                        // Spacious 2x2 Telemetry Cards Grid (No text cramping, wrapping, or bottom-line clipping!)
                        val telemetryCards = buildList {
                            if (settingsStore.dynamicIslandShowPing) {
                                add(
                                    IslandTelemetryCardItem(
                                        label = s.islandLatency,
                                        value = pingLatency?.let { s.language.formatPingLocalized(it) } ?: "\u2014",
                                        valueColor = if (pingLatency != null) statusColors.connected else contentColor,
                                    )
                                )
                            }
                            if (settingsStore.dynamicIslandShowTimer) {
                                add(
                                    IslandTelemetryCardItem(
                                        label = s.islandDuration,
                                        value = if (state == ConnectionState.CONNECTED) {
                                            s.language.formatDurationLocalized(elapsedSeconds, alwaysShowHours = true)
                                        } else {
                                            s.language.formatDurationLocalized(0L, alwaysShowHours = true)
                                        },
                                        valueColor = contentColor,
                                    )
                                )
                            }
                            if (settingsStore.dynamicIslandShowSessionData) {
                                add(
                                    IslandTelemetryCardItem(
                                        label = s.dataUsedLabel,
                                        value = s.language.formatBytesLocalized(liveStats.totalRxBytes + liveStats.totalTxBytes),
                                        valueColor = contentColor,
                                    )
                                )
                            }
                            if (settingsStore.dynamicIslandShowSpeed) {
                                val tx = s.language.formatSpeedLocalized(liveStats.txRateBytesPerSec)
                                val rx = s.language.formatSpeedLocalized(liveStats.rxRateBytesPerSec)
                                add(
                                    IslandTelemetryCardItem(
                                        label = s.islandLiveSpeed,
                                        value = "↓$rx  ↑$tx",
                                        valueColor = if (state == ConnectionState.CONNECTED) statusColors.connected else contentColor,
                                    )
                                )
                            }
                        }

                        if (telemetryCards.isNotEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                telemetryCards.chunked(2).forEach { rowItems ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        rowItems.forEach { item ->
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .background(contentColor.copy(alpha = 0.08f))
                                                    .border(
                                                        width = 0.8.dp,
                                                        color = contentColor.copy(alpha = 0.10f),
                                                        shape = RoundedCornerShape(14.dp),
                                                    )
                                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                            ) {
                                                Text(
                                                    text = item.label,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontFamily = fonts.bodyFont,
                                                        fontWeight = FontWeight.Medium,
                                                        lineHeight = 17.sp,
                                                    ),
                                                    color = contentColor.copy(alpha = 0.68f),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.padding(bottom = 3.dp),
                                                )
                                                Text(
                                                    text = item.value,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontFamily = fonts.monoFont,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp,
                                                        lineHeight = 19.sp,
                                                        fontFeatureSettings = "tnum",
                                                    ),
                                                    color = item.valueColor,
                                                    maxLines = 1,
                                                    softWrap = false,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.padding(vertical = 1.dp),
                                                )
                                            }
                                        }
                                        if (rowItems.size == 1) {
                                            Spacer(Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }

                        // Pinned Quick-Launch Apps Row (Respects dynamicIslandShowQuickApps toggle + supports adding new apps)
                        AnimatedVisibility(
                            visible = settingsStore.dynamicIslandShowQuickApps && (pinnedApps.isNotEmpty() || !isSystemOverlay),
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
                                                        } else {
                                                            val fallbackUrl = KNOWN_APP_WEB_FALLBACKS[appItem.packageName]
                                                            if (fallbackUrl != null) {
                                                                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)).apply {
                                                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                                }
                                                                context.startActivity(webIntent)
                                                                isExpanded = false
                                                            }
                                                        }
                                                    }
                                                }
                                                .padding(horizontal = 11.dp, vertical = 7.dp),
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
                                                        lineHeight = 16.sp,
                                                    ),
                                                    color = contentColor,
                                                    maxLines = 1,
                                                )
                                            }
                                        }
                                    }

                                    if (!isSystemOverlay) {
                                        // Direct "+ Add App" button right inside the quick-apps bar
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(neu.accent.copy(alpha = 0.22f))
                                                .clickable { showDirectAppPicker = true }
                                                .padding(horizontal = 11.dp, vertical = 7.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Filled.Add,
                                                    contentDescription = s.islandAddAppsButton,
                                                    tint = contentColor,
                                                    modifier = Modifier.size(14.dp),
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text(
                                                    text = s.islandAddAppsButton,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontFamily = fonts.bodyFont,
                                                        fontWeight = FontWeight.Bold,
                                                        lineHeight = 16.sp,
                                                    ),
                                                    color = contentColor,
                                                    maxLines = 1,
                                                )
                                            }
                                        }

                                        // Customize Island Studio button
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(contentColor.copy(alpha = 0.12f))
                                                .clickable { showCustomizeModal = true }
                                                .padding(horizontal = 11.dp, vertical = 7.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Filled.Tune,
                                                    contentDescription = s.islandCustomizeButton,
                                                    tint = contentColor,
                                                    modifier = Modifier.size(14.dp),
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text(
                                                    text = s.islandCustomizeButton,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontFamily = fonts.bodyFont,
                                                        fontWeight = FontWeight.SemiBold,
                                                        lineHeight = 16.sp,
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
                                        else neu.accent.copy(alpha = 0.30f),
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

    var showQuickApps by remember { mutableStateOf(settingsStore.dynamicIslandShowQuickApps) }
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

                // 2. Dynamic Island Motion & Physics Animation Selector (12 Styles)
                Text(
                    text = s.islandMotionStyleTitle,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = fonts.headingFont,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = neu.textPrimary,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = s.islandMotionStyleSub,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                    color = neu.textSecondary,
                )
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    DynamicIslandMotionStyle.entries.forEach { item ->
                        val selected = motionStyle == item
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selected) neu.accent else neu.accent.copy(alpha = 0.12f))
                                .clickable {
                                    motionStyle = item
                                    settingsStore.dynamicIslandMotionStyle = item
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                        ) {
                            Text(
                                text = item.localizedLabel(s.language),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = fonts.headingFont,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = if (selected) Color.White else neu.textPrimary,
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = neu.textSecondary.copy(alpha = 0.14f),
                    modifier = Modifier.padding(vertical = 14.dp),
                )

                // 3. Automotive RGB Chasing LED Border Controls
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
                            text = s.language.formatPercentLocalized((ledSpeed * 100).roundToInt()),
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

                // 4. Touch-Drag Free Repositioning & Reset Position
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

                // 5. Pinned Quick-Launch Apps (Enable/Disable Toggle + Manage/Add Apps)
                IslandModalSwitchRow(
                    title = s.islandShowQuickAppsTitle,
                    checked = showQuickApps,
                    onCheckedChange = {
                        showQuickApps = it
                        settingsStore.dynamicIslandShowQuickApps = it
                    },
                )
                Text(
                    text = s.islandShowQuickAppsSub,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                    color = neu.textSecondary,
                )
                if (showQuickApps) {
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
                }

                HorizontalDivider(
                    color = neu.textSecondary.copy(alpha = 0.14f),
                    modifier = Modifier.padding(vertical = 14.dp),
                )

                // 6. Granular Displayed Content Toggles
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DynamicIslandAppPickerDialog(
    current: Set<String>,
    onDismiss: () -> Unit,
    onConfirm: (Set<String>) -> Unit,
) {
    val context = LocalContext.current
    val s = currentFoxyStrings
    val neu = currentNeumorphicColors
    val fonts = currentFoxyFonts
    var selected by remember { mutableStateOf(current) }
    var searchQuery by remember { mutableStateOf("") }

    // Query ALL launchable apps on the device (including pre-installed YouTube, Chrome, etc.)
    // plus merge known popular presets so the user can always toggle YouTube, Telegram, Chrome, Instagram, WhatsApp, X, Spotify, etc.
    val installedApps by produceState<List<IslandPinnedAppInfo>>(initialValue = emptyList()) {
        value = withContext(Dispatchers.IO) {
            val pm = context.packageManager
            val launcherIntent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
            val resolved = runCatching { pm.queryIntentActivities(launcherIntent, 0) }.getOrDefault(emptyList())
            val fromDevice = resolved
                .mapNotNull { resolveInfo ->
                    val pkg = resolveInfo.activityInfo?.packageName ?: return@mapNotNull null
                    if (pkg == context.packageName) return@mapNotNull null
                    val label = runCatching { resolveInfo.loadLabel(pm).toString() }.getOrDefault(pkg)
                    IslandPinnedAppInfo(pkg, label)
                }
                .distinctBy { it.packageName }

            val presetExtras = KNOWN_APP_FALLBACK_LABELS.entries
                .filter { (pkg, _) -> fromDevice.none { it.packageName == pkg } }
                .map { (pkg, label) -> IslandPinnedAppInfo(pkg, label) }

            (presetExtras + fromDevice).sortedWith(
                compareByDescending<IslandPinnedAppInfo> { KNOWN_APP_FALLBACK_LABELS.containsKey(it.packageName) }
                    .thenBy { it.label.lowercase() }
            )
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
        title = {
            Column {
                Text(
                    text = s.islandPinnedAppsTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = fonts.headingFont,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = s.islandPinnedAppsSub,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        text = {
            Column(Modifier.height(420.dp)) {
                // Quick Popular App Chips
                Text(
                    text = when (s.language) {
                        AppLanguage.FA -> "برنامه‌های محبوب (انتخاب سریع):"
                        AppLanguage.AR -> "التطبيقات الشائعة (اختيار سريع):"
                        AppLanguage.EN -> "Popular Quick Shortcuts:"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = fonts.headingFont,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = neu.textSecondary,
                    modifier = Modifier.padding(bottom = 6.dp),
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    KNOWN_APP_FALLBACK_LABELS.forEach { (pkg, label) ->
                        val isPinned = selected.contains(pkg)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isPinned) neu.accent else neu.accent.copy(alpha = 0.12f))
                                .clickable {
                                    selected = if (isPinned) selected - pkg else selected + pkg
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                        ) {
                            Text(
                                text = if (isPinned) "✓ $label" else "+ $label",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = fonts.headingFont,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = if (isPinned) Color.White else neu.textPrimary,
                            )
                        }
                    }
                }

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
                Text(s.save, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(s.cancel)
            }
        },
    )
}

