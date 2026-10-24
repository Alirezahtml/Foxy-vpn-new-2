package com.vauth.foxyvpn.ui.screens

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vauth.foxyvpn.BuildConfig
import com.vauth.foxyvpn.data.SettingsStore
import com.vauth.foxyvpn.quicksettings.DynamicIslandOverlayService
import com.vauth.foxyvpn.ui.components.DynamicIslandAppPickerDialog
import com.vauth.foxyvpn.ui.components.DynamicIslandCustomizationModal
import com.vauth.foxyvpn.ui.theme.AppLanguage
import com.vauth.foxyvpn.ui.theme.LanguageController
import com.vauth.foxyvpn.ui.theme.NeumorphicCard
import com.vauth.foxyvpn.ui.theme.NeumorphicIconButton
import com.vauth.foxyvpn.ui.theme.ThemeController
import com.vauth.foxyvpn.ui.theme.ThemeMode
import com.vauth.foxyvpn.ui.theme.currentFoxyFonts
import com.vauth.foxyvpn.ui.theme.currentFoxyStrings
import com.vauth.foxyvpn.ui.theme.currentNeumorphicColors
import com.vauth.foxyvpn.ui.theme.formatPercentLocalized
import com.vauth.foxyvpn.ui.theme.localizeDigits
import com.vauth.foxyvpn.ui.theme.localizeNumber
import com.vauth.foxyvpn.ui.theme.neumorphicSunken
import com.vauth.foxyvpn.ui.theme.neumorphicTouchBounce
import com.vauth.foxyvpn.ui.theme.staggeredEntrance
import android.app.TimePickerDialog
import android.os.PowerManager
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.CircleShape
import com.vauth.foxyvpn.data.DynamicIslandLedStyle
import com.vauth.foxyvpn.data.DynamicIslandMotionStyle
import com.vauth.foxyvpn.data.FoxyColorTheme
import com.vauth.foxyvpn.ui.theme.formatClockTimeLocalized
import com.vauth.foxyvpn.ui.theme.formatMinutesLocalized
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

private const val INSTAGRAM_URL = "https://instagram.com/Aliem061"
private const val TELEGRAM_URL = "https://t.me/aliem_061"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    settingsStore: SettingsStore,
    themeController: ThemeController,
    languageController: LanguageController,
    onOpenLogs: () -> Unit,
    onOpenTools: (Int) -> Unit = {},
    onSignOut: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val neu = currentNeumorphicColors
    val s = currentFoxyStrings
    val fonts = currentFoxyFonts

    var exitCheckEnabled by remember { mutableStateOf(settingsStore.exitCheckEnabled) }
    var dnsProvider by remember { mutableStateOf(settingsStore.dnsProvider) }
    var customDohUrl by remember { mutableStateOf(settingsStore.customDohUrl) }
    var bootstrapDnsProvider by remember { mutableStateOf(settingsStore.bootstrapDnsProvider) }
    var customBootstrapIp by remember { mutableStateOf(settingsStore.customBootstrapIp) }
    var socksBindAddress by remember { mutableStateOf(settingsStore.socksBindAddress) }
    var socksPort by remember { mutableStateOf(settingsStore.socksPort) }
    var httpProxyEnabled by remember { mutableStateOf(settingsStore.httpProxyEnabled) }
    var httpProxyPort by remember { mutableStateOf(settingsStore.httpProxyPort) }
    var proxyOnlyMode by remember { mutableStateOf(settingsStore.proxyOnlyMode) }
    var customEdgeAddress by remember { mutableStateOf(settingsStore.customEdgeAddress) }
    var upstreamProxyEnabled by remember { mutableStateOf(settingsStore.upstreamProxyEnabled) }
    var upstreamProxyType by remember { mutableStateOf(settingsStore.upstreamProxyType) }
    var upstreamProxyHost by remember { mutableStateOf(settingsStore.upstreamProxyHost) }
    var upstreamProxyPort by remember { mutableStateOf(settingsStore.upstreamProxyPort) }
    var upstreamProxyUsername by remember { mutableStateOf(settingsStore.upstreamProxyUsername) }
    var upstreamProxyPassword by remember { mutableStateOf(settingsStore.upstreamProxyPassword) }
    var excludedApps by remember { mutableStateOf(settingsStore.excludedApps) }
    var mtu by remember { mutableStateOf(settingsStore.mtu) }
    var tlsSplitMode by remember { mutableStateOf(settingsStore.tlsSplitMode) }

    // Dynamic Island State
    var dynamicIslandEnabled by remember { mutableStateOf(settingsStore.dynamicIslandEnabled) }
    var dynamicIslandOverlayEnabled by remember { mutableStateOf(settingsStore.dynamicIslandOverlayEnabled) }
    var dynamicIslandSize by remember { mutableStateOf(settingsStore.dynamicIslandSize) }
    var dynamicIslandScalePercent by remember { mutableFloatStateOf(settingsStore.dynamicIslandScalePercent.toFloat()) }
    var dynamicIslandCornerRadius by remember { mutableStateOf(settingsStore.dynamicIslandCornerRadius) }
    var dynamicIslandPosition by remember { mutableStateOf(settingsStore.dynamicIslandPosition) }
    var dynamicIslandTheme by remember { mutableStateOf(settingsStore.dynamicIslandTheme) }
    var dynamicIslandShowSpeed by remember { mutableStateOf(settingsStore.dynamicIslandShowSpeed) }
    var dynamicIslandShowPing by remember { mutableStateOf(settingsStore.dynamicIslandShowPing) }
    var dynamicIslandShowFlag by remember { mutableStateOf(settingsStore.dynamicIslandShowFlag) }
    var dynamicIslandShowTimer by remember { mutableStateOf(settingsStore.dynamicIslandShowTimer) }
    var dynamicIslandAnimation by remember { mutableStateOf(settingsStore.dynamicIslandAnimation) }
    var dynamicIslandPinnedApps by remember { mutableStateOf(settingsStore.dynamicIslandPinnedApps) }
    var dynamicIslandLedBorderEnabled by remember { mutableStateOf(settingsStore.dynamicIslandLedBorderEnabled) }
    var dynamicIslandLedStyle by remember { mutableStateOf(settingsStore.dynamicIslandLedStyle) }
    var dynamicIslandMotionStyle by remember { mutableStateOf(settingsStore.dynamicIslandMotionStyle) }
    var dynamicIslandLedSpeed by remember { mutableFloatStateOf(settingsStore.dynamicIslandLedSpeed) }
    var dynamicIslandShowTowerWave by remember { mutableStateOf(settingsStore.dynamicIslandShowTowerWave) }
    var dynamicIslandShowServerName by remember { mutableStateOf(settingsStore.dynamicIslandShowServerName) }
    var dynamicIslandShowSessionData by remember { mutableStateOf(settingsStore.dynamicIslandShowSessionData) }
    var dynamicIslandShowIpBadge by remember { mutableStateOf(settingsStore.dynamicIslandShowIpBadge) }
    var dynamicIslandDraggable by remember { mutableStateOf(settingsStore.dynamicIslandDraggable) }

    // Speed Booster, Smart Battery Saver & Automation State
    var unmeteredVpnModeEnabled by remember { mutableStateOf(settingsStore.unmeteredVpnModeEnabled) }
    var tcpBufferBoostEnabled by remember { mutableStateOf(settingsStore.tcpBufferBoostEnabled) }
    var batterySaverModeEnabled by remember { mutableStateOf(settingsStore.batterySaverModeEnabled) }
    var scheduleEnabled by remember { mutableStateOf(settingsStore.scheduleEnabled) }
    var scheduleStartHour by remember { mutableStateOf(settingsStore.scheduleStartHour) }
    var scheduleStartMinute by remember { mutableStateOf(settingsStore.scheduleStartMinute) }
    var scheduleEndHour by remember { mutableStateOf(settingsStore.scheduleEndHour) }
    var scheduleEndMinute by remember { mutableStateOf(settingsStore.scheduleEndMinute) }
    var idleDisconnectEnabled by remember { mutableStateOf(settingsStore.idleDisconnectEnabled) }
    var idleTimeoutMinutes by remember { mutableStateOf(settingsStore.idleTimeoutMinutes) }
    var sessionDataLimitEnabled by remember { mutableStateOf(settingsStore.sessionDataLimitEnabled) }
    var sessionDataLimitMb by remember { mutableStateOf(settingsStore.sessionDataLimitMb) }
    var smartPingAutoSwitch by remember { mutableStateOf(settingsStore.smartPingAutoSwitch) }
    var autoConnectOnLaunch by remember { mutableStateOf(settingsStore.autoConnectOnLaunch) }
    var stealthNotificationEnabled by remember { mutableStateOf(settingsStore.stealthNotificationEnabled) }

    // Dialog toggles
    var showDnsProviderDialog by remember { mutableStateOf(false) }
    var showBootstrapDnsDialog by remember { mutableStateOf(false) }
    var showMtuDialog by remember { mutableStateOf(false) }
    var showSocksBindDialog by remember { mutableStateOf(false) }
    var showSocksPortDialog by remember { mutableStateOf(false) }
    var showHttpPortDialog by remember { mutableStateOf(false) }
    var showEdgeAddressDialog by remember { mutableStateOf(false) }
    var showUpstreamProxyTypeDialog by remember { mutableStateOf(false) }
    var showUpstreamProxyAddressDialog by remember { mutableStateOf(false) }
    var showUpstreamProxyCredentialsDialog by remember { mutableStateOf(false) }
    var showSplitTunnelDialog by remember { mutableStateOf(false) }
    var showTlsSplitDialog by remember { mutableStateOf(false) }
    var showIslandSizeDialog by remember { mutableStateOf(false) }
    var showIslandCornerRadiusDialog by remember { mutableStateOf(false) }
    var showIslandPositionDialog by remember { mutableStateOf(false) }
    var showIslandThemeDialog by remember { mutableStateOf(false) }
    var showIslandPinnedAppsDialog by remember { mutableStateOf(false) }
    var showIslandStudioModal by remember { mutableStateOf(false) }
    var showIdleTimeoutDialog by remember { mutableStateOf(false) }
    var showDataCapDialog by remember { mutableStateOf(false) }

    if (showIslandStudioModal) {
        DynamicIslandCustomizationModal(
            settingsStore = settingsStore,
            onDismiss = {
                dynamicIslandScalePercent = settingsStore.dynamicIslandScalePercent.toFloat()
                dynamicIslandShowSpeed = settingsStore.dynamicIslandShowSpeed
                dynamicIslandShowPing = settingsStore.dynamicIslandShowPing
                dynamicIslandShowFlag = settingsStore.dynamicIslandShowFlag
                dynamicIslandShowTimer = settingsStore.dynamicIslandShowTimer
                dynamicIslandAnimation = settingsStore.dynamicIslandAnimation
                dynamicIslandPinnedApps = settingsStore.dynamicIslandPinnedApps
                dynamicIslandLedBorderEnabled = settingsStore.dynamicIslandLedBorderEnabled
                dynamicIslandLedStyle = settingsStore.dynamicIslandLedStyle
                dynamicIslandMotionStyle = settingsStore.dynamicIslandMotionStyle
                dynamicIslandLedSpeed = settingsStore.dynamicIslandLedSpeed
                dynamicIslandShowTowerWave = settingsStore.dynamicIslandShowTowerWave
                dynamicIslandShowServerName = settingsStore.dynamicIslandShowServerName
                dynamicIslandShowSessionData = settingsStore.dynamicIslandShowSessionData
                dynamicIslandShowIpBadge = settingsStore.dynamicIslandShowIpBadge
                dynamicIslandDraggable = settingsStore.dynamicIslandDraggable
                showIslandStudioModal = false
            },
        )
    }

    if (showIslandPinnedAppsDialog) {
        DynamicIslandAppPickerDialog(
            current = dynamicIslandPinnedApps,
            onDismiss = { showIslandPinnedAppsDialog = false },
            onConfirm = {
                dynamicIslandPinnedApps = it
                settingsStore.dynamicIslandPinnedApps = it
                showIslandPinnedAppsDialog = false
            },
        )
    }

    if (showIslandCornerRadiusDialog) {
        DynamicIslandCornerRadiusDialog(
            current = dynamicIslandCornerRadius,
            onDismiss = { showIslandCornerRadiusDialog = false },
            onConfirm = {
                dynamicIslandCornerRadius = it
                settingsStore.dynamicIslandCornerRadius = it
                showIslandCornerRadiusDialog = false
            },
        )
    }

    if (showIslandSizeDialog) {
        DynamicIslandSizeDialog(
            current = dynamicIslandSize,
            onDismiss = { showIslandSizeDialog = false },
            onConfirm = {
                dynamicIslandSize = it
                settingsStore.dynamicIslandSize = it
                showIslandSizeDialog = false
            },
        )
    }

    if (showIslandPositionDialog) {
        DynamicIslandPositionDialog(
            current = dynamicIslandPosition,
            onDismiss = { showIslandPositionDialog = false },
            onConfirm = {
                dynamicIslandPosition = it
                settingsStore.dynamicIslandPosition = it
                showIslandPositionDialog = false
            },
        )
    }

    if (showIslandThemeDialog) {
        DynamicIslandThemeDialog(
            current = dynamicIslandTheme,
            onDismiss = { showIslandThemeDialog = false },
            onConfirm = {
                dynamicIslandTheme = it
                settingsStore.dynamicIslandTheme = it
                showIslandThemeDialog = false
            },
        )
    }

    if (showMtuDialog) {
        MtuPickerDialog(
            current = mtu,
            onDismiss = { showMtuDialog = false },
            onConfirm = {
                mtu = it
                settingsStore.mtu = it
                showMtuDialog = false
            },
        )
    }

    if (showDnsProviderDialog) {
        DnsProviderPickerDialog(
            currentProvider = dnsProvider,
            currentCustomUrl = customDohUrl,
            onDismiss = { showDnsProviderDialog = false },
            onConfirm = { provider, url ->
                dnsProvider = provider
                settingsStore.dnsProvider = provider
                customDohUrl = url
                settingsStore.customDohUrl = url
                showDnsProviderDialog = false
            },
        )
    }
    if (showBootstrapDnsDialog) {
        BootstrapDnsPickerDialog(
            currentProvider = bootstrapDnsProvider,
            currentCustomIp = customBootstrapIp,
            onDismiss = { showBootstrapDnsDialog = false },
            onConfirm = { provider, ip ->
                bootstrapDnsProvider = provider
                settingsStore.bootstrapDnsProvider = provider
                customBootstrapIp = ip
                settingsStore.customBootstrapIp = ip
                showBootstrapDnsDialog = false
            },
        )
    }
    if (showSocksBindDialog) {
        SocksBindAddressPickerDialog(
            current = socksBindAddress,
            onDismiss = { showSocksBindDialog = false },
            onConfirm = {
                socksBindAddress = it
                settingsStore.socksBindAddress = it
                showSocksBindDialog = false
            },
        )
    }
    if (showSocksPortDialog) {
        SocksPortPickerDialog(
            current = socksPort,
            onDismiss = { showSocksPortDialog = false },
            onConfirm = {
                socksPort = it
                settingsStore.socksPort = it
                showSocksPortDialog = false
            },
        )
    }
    if (showHttpPortDialog) {
        HttpPortPickerDialog(
            current = httpProxyPort,
            onDismiss = { showHttpPortDialog = false },
            onConfirm = {
                httpProxyPort = it
                settingsStore.httpProxyPort = it
                showHttpPortDialog = false
            },
        )
    }
    if (showEdgeAddressDialog) {
        EdgeAddressPickerDialog(
            current = customEdgeAddress,
            onDismiss = { showEdgeAddressDialog = false },
            onConfirm = {
                customEdgeAddress = it
                settingsStore.customEdgeAddress = it
                showEdgeAddressDialog = false
            },
        )
    }
    if (showUpstreamProxyTypeDialog) {
        UpstreamProxyTypeDialog(
            current = upstreamProxyType,
            onDismiss = { showUpstreamProxyTypeDialog = false },
            onConfirm = {
                upstreamProxyType = it
                settingsStore.upstreamProxyType = it
                showUpstreamProxyTypeDialog = false
            },
        )
    }
    if (showUpstreamProxyAddressDialog) {
        UpstreamProxyAddressDialog(
            currentHost = upstreamProxyHost,
            currentPort = upstreamProxyPort,
            onDismiss = { showUpstreamProxyAddressDialog = false },
            onConfirm = { host, port ->
                upstreamProxyHost = host
                upstreamProxyPort = port
                settingsStore.upstreamProxyHost = host
                settingsStore.upstreamProxyPort = port
                showUpstreamProxyAddressDialog = false
            },
        )
    }
    if (showUpstreamProxyCredentialsDialog) {
        UpstreamProxyCredentialsDialog(
            currentUsername = upstreamProxyUsername,
            currentPassword = upstreamProxyPassword,
            onDismiss = { showUpstreamProxyCredentialsDialog = false },
            onConfirm = { username, password ->
                upstreamProxyUsername = username
                upstreamProxyPassword = password
                settingsStore.upstreamProxyUsername = username
                settingsStore.upstreamProxyPassword = password
                showUpstreamProxyCredentialsDialog = false
            },
        )
    }
    if (showSplitTunnelDialog) {
        SplitTunnelDialog(
            current = excludedApps,
            onDismiss = { showSplitTunnelDialog = false },
            onConfirm = {
                excludedApps = it
                settingsStore.excludedApps = it
                showSplitTunnelDialog = false
            },
        )
    }
    if (showTlsSplitDialog) {
        TlsSplitModePickerDialog(
            current = tlsSplitMode,
            onDismiss = { showTlsSplitDialog = false },
            onConfirm = {
                tlsSplitMode = it
                settingsStore.tlsSplitMode = it
                showTlsSplitDialog = false
            },
        )
    }

    Scaffold(
        containerColor = neu.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        s.settingsTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = fonts.headingFont,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = neu.textPrimary,
                    )
                },
                navigationIcon = {
                    NeumorphicIconButton(
                        size = 42.dp,
                        onClick = onBack,
                        modifier = Modifier.padding(start = 8.dp),
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = s.back,
                            tint = neu.textPrimary,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = neu.background),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(modifier = Modifier.widthIn(max = 600.dp)) {
                Column {
                    // 0. LANGUAGE (EN / FA / AR) & APPEARANCE SECTION
                    SectionHeading(s.sectionLanguageAndTheme)
                    NeumorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .staggeredEntrance(0),
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Text(
                                s.appLanguageTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = fonts.headingFont,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                color = neu.textPrimary,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                s.appLanguageSubtitle,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                                color = neu.textSecondary,
                            )
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                AppLanguage.entries.forEach { lang ->
                                    val isSelected = languageController.language == lang
                                    val pillShape = RoundedCornerShape(14.dp)
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .neumorphicTouchBounce()
                                            .let { mod ->
                                                if (isSelected) {
                                                    mod.clip(pillShape).background(neu.accent)
                                                } else {
                                                    mod.neumorphicSunken(
                                                        shape = pillShape,
                                                        cornerRadius = 14.dp,
                                                        depth = 3.dp,
                                                        surfaceColor = neu.surfaceElevated,
                                                    )
                                                }
                                            }
                                            .clickable { languageController.set(lang) }
                                            .padding(vertical = 12.dp, horizontal = 8.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = lang.nativeName,
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                            ),
                                            color = if (isSelected) Color.White else neu.textPrimary,
                                            maxLines = 1,
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(
                                color = neu.textSecondary.copy(alpha = 0.12f),
                                modifier = Modifier.padding(vertical = 16.dp),
                            )

                            Text(
                                s.appThemeTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = fonts.headingFont,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                color = neu.textPrimary,
                            )
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                val themeOptions = listOf(
                                    ThemeMode.SYSTEM to s.themeSystem,
                                    ThemeMode.LIGHT to s.themeLight,
                                    ThemeMode.DARK to s.themeDark,
                                )
                                themeOptions.forEach { (mode, label) ->
                                    val isSelected = themeController.mode == mode
                                    val pillShape = RoundedCornerShape(14.dp)
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .neumorphicTouchBounce()
                                            .let { mod ->
                                                if (isSelected) {
                                                    mod.clip(pillShape).background(neu.accent)
                                                } else {
                                                    mod.neumorphicSunken(
                                                        shape = pillShape,
                                                        cornerRadius = 14.dp,
                                                        depth = 3.dp,
                                                        surfaceColor = neu.surfaceElevated,
                                                    )
                                                }
                                            }
                                            .clickable { themeController.set(mode) }
                                            .padding(vertical = 11.dp, horizontal = 8.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontFamily = fonts.headingFont,
                                                fontWeight = FontWeight.Bold,
                                            ),
                                            color = if (isSelected) Color.White else neu.textPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(
                                color = neu.textSecondary.copy(alpha = 0.12f),
                                modifier = Modifier.padding(vertical = 16.dp),
                            )

                            // 6 User-Friendly Color Combinations (FoxyColorTheme)
                            Text(
                                text = s.colorPaletteTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = fonts.headingFont,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                color = neu.textPrimary,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = s.colorPaletteSub,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                                color = neu.textSecondary,
                            )
                            Spacer(Modifier.height(12.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                FoxyColorTheme.entries.forEach { palette ->
                                    val selected = themeController.colorTheme == palette
                                    val swatchColor = when (palette) {
                                        FoxyColorTheme.CYBER_FOX -> Color(0xFFFF6D3A)
                                        FoxyColorTheme.OCEAN_SAPPHIRE -> Color(0xFF0EA5E9)
                                        FoxyColorTheme.EMERALD_AURORA -> Color(0xFF10B981)
                                        FoxyColorTheme.ROYAL_AMETHYST -> Color(0xFF8B5CF6)
                                        FoxyColorTheme.ROSE_VELVET -> Color(0xFFF43F5E)
                                        FoxyColorTheme.SOLAR_GOLD -> Color(0xFFF59E0B)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .neumorphicTouchBounce()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                if (selected) swatchColor.copy(alpha = 0.22f)
                                                else neu.surfaceElevated,
                                            )
                                            .border(
                                                width = if (selected) 1.8.dp else 1.dp,
                                                color = if (selected) swatchColor else neu.textSecondary.copy(alpha = 0.18f),
                                                shape = RoundedCornerShape(14.dp),
                                            )
                                            .clickable { themeController.applyColorTheme(palette) }
                                            .padding(horizontal = 12.dp, vertical = 9.dp),
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .clip(CircleShape)
                                                    .background(swatchColor),
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                text = palette.localizedLabel(s.language),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontFamily = fonts.headingFont,
                                                    fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                                ),
                                                color = if (selected) swatchColor else neu.textPrimary,
                                                maxLines = 1,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 1. SMART NETWORK & SECURITY TOOLKIT (5 OPTIONS)
                    SectionHeading(s.sectionSmartToolkit)
                    NeumorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .staggeredEntrance(1),
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            SettingClickableRow(
                                title = s.toolSpeedTitle,
                                value = s.toolSpeedSubtitle,
                                onClick = { onOpenTools(0) },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.toolLeakTitle,
                                value = s.toolLeakSubtitle,
                                onClick = { onOpenTools(1) },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.toolScannerTitle,
                                value = s.toolScannerSubtitle,
                                onClick = { onOpenTools(2) },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.toolWatchdogTitle,
                                value = s.toolWatchdogSubtitle,
                                onClick = { onOpenTools(3) },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.toolMtuTitle,
                                value = s.toolMtuSubtitle,
                                onClick = { onOpenTools(4) },
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 2. DYNAMIC ISLAND & FULL CUSTOMIZATION SECTION
                    SectionHeading(s.sectionDynamicIsland)
                    NeumorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .staggeredEntrance(2),
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            SettingToggleRow(
                                title = s.enableDynamicIslandTitle,
                                subtitle = s.enableDynamicIslandSub,
                                checked = dynamicIslandEnabled,
                                onCheckedChange = {
                                    dynamicIslandEnabled = it
                                    settingsStore.dynamicIslandEnabled = it
                                },
                            )

                            if (dynamicIslandEnabled) {
                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                // Open Full Dynamic Island Studio Popup Button
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .neumorphicTouchBounce()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(neu.accent.copy(alpha = 0.15f))
                                        .clickable { showIslandStudioModal = true }
                                        .padding(vertical = 12.dp, horizontal = 14.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.Tune,
                                            contentDescription = null,
                                            tint = neu.accent,
                                            modifier = Modifier.size(18.dp),
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = s.islandCustomizeButton,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontFamily = fonts.headingFont,
                                                fontWeight = FontWeight.Bold,
                                            ),
                                            color = neu.accent,
                                        )
                                    }
                                }

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 14.dp))

                                // Linear Manual Size Slider (0% to 100%)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = s.islandManualScaleTitle,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontFamily = fonts.headingFont,
                                            fontWeight = FontWeight.SemiBold,
                                        ),
                                        color = neu.textPrimary,
                                        modifier = Modifier.weight(1f),
                                    )
                                    Text(
                                        text = s.language.formatPercentLocalized(dynamicIslandScalePercent.roundToInt()),
                                        style = MaterialTheme.typography.titleMedium.copy(
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
                                    value = dynamicIslandScalePercent,
                                    onValueChange = { next ->
                                        dynamicIslandScalePercent = next
                                        settingsStore.dynamicIslandScalePercent = next.roundToInt()
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

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                // Pinned Quick-Launch Apps inside Dynamic Island
                                SettingClickableRow(
                                    title = s.islandPinnedAppsTitle,
                                    value = if (dynamicIslandPinnedApps.isEmpty()) s.islandNoPinnedApps
                                    else "${s.islandAddAppsButton} (${s.language.localizeNumber(dynamicIslandPinnedApps.size)})",
                                    onClick = { showIslandPinnedAppsDialog = true },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                val hasOverlayPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context)
                                SettingToggleRow(
                                    title = s.overlayIslandTitle,
                                    subtitle = if (hasOverlayPermission) s.overlayIslandSubGranted
                                    else s.overlayIslandSubNeedsPerm,
                                    checked = dynamicIslandOverlayEnabled && hasOverlayPermission,
                                    onCheckedChange = { enable ->
                                        if (enable) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                                                val intent = Intent(
                                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                    Uri.parse("package:${context.packageName}"),
                                                )
                                                context.startActivity(intent)
                                            } else {
                                                dynamicIslandOverlayEnabled = true
                                                settingsStore.dynamicIslandOverlayEnabled = true
                                                DynamicIslandOverlayService.start(context)
                                            }
                                        } else {
                                            dynamicIslandOverlayEnabled = false
                                            settingsStore.dynamicIslandOverlayEnabled = false
                                            DynamicIslandOverlayService.stop(context)
                                        }
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingClickableRow(
                                    title = s.islandSizeTitle,
                                    value = dynamicIslandSize.localizedLabel(s.language),
                                    onClick = { showIslandSizeDialog = true },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingClickableRow(
                                    title = s.islandCornerRadiusTitle,
                                    value = s.language.localizeNumber(dynamicIslandCornerRadius),
                                    onClick = { showIslandCornerRadiusDialog = true },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingClickableRow(
                                    title = s.islandPositionTitle,
                                    value = dynamicIslandPosition.localizedLabel(s.language),
                                    onClick = { showIslandPositionDialog = true },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingClickableRow(
                                    title = s.islandThemeTitle,
                                    value = dynamicIslandTheme.localizedLabel(s.language),
                                    onClick = { showIslandThemeDialog = true },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                // Smooth Motion & Physics Style (8 Presets)
                                Text(
                                    text = s.islandMotionStyleTitle,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = fonts.headingFont,
                                        fontWeight = FontWeight.SemiBold,
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
                                    DynamicIslandMotionStyle.entries.forEach { motionPreset ->
                                        val isSelected = dynamicIslandMotionStyle == motionPreset
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSelected) neu.accent else neu.accent.copy(alpha = 0.12f))
                                                .clickable {
                                                    dynamicIslandMotionStyle = motionPreset
                                                    settingsStore.dynamicIslandMotionStyle = motionPreset
                                                    dynamicIslandAnimation = true
                                                    settingsStore.dynamicIslandAnimation = true
                                                }
                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                        ) {
                                            Text(
                                                text = motionPreset.localizedLabel(s.language),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontFamily = fonts.headingFont,
                                                    fontWeight = FontWeight.Bold,
                                                ),
                                                color = if (isSelected) Color.White else neu.textPrimary,
                                            )
                                        }
                                    }
                                }

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                // Automotive RGB Chasing LED Border (14 Presets)
                                SettingToggleRow(
                                    title = s.islandLedBorderTitle,
                                    subtitle = s.islandLedBorderSub,
                                    checked = dynamicIslandLedBorderEnabled,
                                    onCheckedChange = {
                                        dynamicIslandLedBorderEnabled = it
                                        settingsStore.dynamicIslandLedBorderEnabled = it
                                    },
                                )
                                if (dynamicIslandLedBorderEnabled) {
                                    Spacer(Modifier.height(10.dp))
                                    FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        DynamicIslandLedStyle.entries.forEach { stylePreset ->
                                            val isSelected = dynamicIslandLedStyle == stylePreset
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(if (isSelected) neu.accent else neu.accent.copy(alpha = 0.12f))
                                                    .clickable {
                                                        dynamicIslandLedStyle = stylePreset
                                                        settingsStore.dynamicIslandLedStyle = stylePreset
                                                    }
                                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                            ) {
                                                Text(
                                                    text = stylePreset.localizedLabel(s.language),
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontFamily = fonts.headingFont,
                                                        fontWeight = FontWeight.Bold,
                                                    ),
                                                    color = if (isSelected) Color.White else neu.textPrimary,
                                                )
                                            }
                                        }
                                    }
                                }

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                // Signal Tower Broadcast Wave
                                SettingToggleRow(
                                    title = s.islandShowTowerWaveTitle,
                                    subtitle = s.islandShowTowerWaveSub,
                                    checked = dynamicIslandShowTowerWave,
                                    onCheckedChange = {
                                        dynamicIslandShowTowerWave = it
                                        settingsStore.dynamicIslandShowTowerWave = it
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                // Touch-Drag Repositioning
                                SettingToggleRow(
                                    title = s.islandDraggableTitle,
                                    subtitle = s.islandDraggableSub,
                                    checked = dynamicIslandDraggable,
                                    onCheckedChange = {
                                        dynamicIslandDraggable = it
                                        settingsStore.dynamicIslandDraggable = it
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingClickableRow(
                                    title = s.islandResetPositionButton,
                                    value = s.islandPositionTitle,
                                    onClick = { settingsStore.resetDynamicIslandPosition() },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingToggleRow(
                                    title = s.islandShowSpeedTitle,
                                    subtitle = s.islandShowSpeedSub,
                                    checked = dynamicIslandShowSpeed,
                                    onCheckedChange = {
                                        dynamicIslandShowSpeed = it
                                        settingsStore.dynamicIslandShowSpeed = it
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingToggleRow(
                                    title = s.islandShowPingTitle,
                                    subtitle = s.islandShowPingSub,
                                    checked = dynamicIslandShowPing,
                                    onCheckedChange = {
                                        dynamicIslandShowPing = it
                                        settingsStore.dynamicIslandShowPing = it
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingToggleRow(
                                    title = s.islandShowFlagTitle,
                                    subtitle = s.islandShowFlagSub,
                                    checked = dynamicIslandShowFlag,
                                    onCheckedChange = {
                                        dynamicIslandShowFlag = it
                                        settingsStore.dynamicIslandShowFlag = it
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingToggleRow(
                                    title = s.islandShowServerNameTitle,
                                    subtitle = s.islandShowServerNameSub,
                                    checked = dynamicIslandShowServerName,
                                    onCheckedChange = {
                                        dynamicIslandShowServerName = it
                                        settingsStore.dynamicIslandShowServerName = it
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingToggleRow(
                                    title = s.islandShowSessionDataTitle,
                                    subtitle = s.islandShowSessionDataSub,
                                    checked = dynamicIslandShowSessionData,
                                    onCheckedChange = {
                                        dynamicIslandShowSessionData = it
                                        settingsStore.dynamicIslandShowSessionData = it
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingToggleRow(
                                    title = s.islandShowIpBadgeTitle,
                                    subtitle = s.islandShowIpBadgeSub,
                                    checked = dynamicIslandShowIpBadge,
                                    onCheckedChange = {
                                        dynamicIslandShowIpBadge = it
                                        settingsStore.dynamicIslandShowIpBadge = it
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingToggleRow(
                                    title = s.islandShowTimerTitle,
                                    subtitle = s.islandShowTimerSub,
                                    checked = dynamicIslandShowTimer,
                                    onCheckedChange = {
                                        dynamicIslandShowTimer = it
                                        settingsStore.dynamicIslandShowTimer = it
                                    },
                                )

                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                                SettingToggleRow(
                                    title = s.islandAnimationTitle,
                                    subtitle = s.islandAnimationSub,
                                    checked = dynamicIslandAnimation,
                                    onCheckedChange = {
                                        dynamicIslandAnimation = it
                                        settingsStore.dynamicIslandAnimation = it
                                    },
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 2B. SPEED BOOSTER, BATTERY SAVER & SMART AUTOMATION
                    SectionHeading(s.sectionSmartAutomation)
                    NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            // 0a. Unmetered High-Speed Tunnel Mode (Android Q+ VpnService.Builder.setMetered(false))
                            SettingToggleRow(
                                title = s.unmeteredSpeedBoostTitle,
                                subtitle = s.unmeteredSpeedBoostSub,
                                checked = unmeteredVpnModeEnabled,
                                onCheckedChange = {
                                    unmeteredVpnModeEnabled = it
                                    settingsStore.unmeteredVpnModeEnabled = it
                                },
                            )

                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                            // 0b. Turbo TCP/UDP Socket Buffer (256KB / 1MB)
                            SettingToggleRow(
                                title = s.tcpBufferBoostTitle,
                                subtitle = s.tcpBufferBoostSub,
                                checked = tcpBufferBoostEnabled,
                                onCheckedChange = {
                                    tcpBufferBoostEnabled = it
                                    settingsStore.tcpBufferBoostEnabled = it
                                },
                            )

                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                            // 0c. Smart Battery Saver (Eco Mode)
                            SettingToggleRow(
                                title = s.batterySaverModeTitle,
                                subtitle = s.batterySaverModeSub,
                                checked = batterySaverModeEnabled,
                                onCheckedChange = {
                                    batterySaverModeEnabled = it
                                    settingsStore.batterySaverModeEnabled = it
                                },
                            )

                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                            // 0d. Unrestricted Background Battery Doze Exemption
                            val powerManager = context.getSystemService(PowerManager::class.java)
                            val isIgnoringBatteryOptimizations = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                runCatching { powerManager?.isIgnoringBatteryOptimizations(context.packageName) == true }.getOrDefault(false)
                            } else {
                                true
                            }
                            SettingClickableRow(
                                title = s.batteryExemptionTitle,
                                value = if (isIgnoringBatteryOptimizations) s.batteryExemptionGrantedSub else s.batteryExemptionRequestSub,
                                onClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        runCatching {
                                            @Suppress("BatteryLife")
                                            val intent = Intent(
                                                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                                                Uri.parse("package:${context.packageName}"),
                                            )
                                            context.startActivity(intent)
                                        }.onFailure {
                                            runCatching {
                                                context.startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
                                            }
                                        }
                                    }
                                },
                            )

                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                            // 1. Scheduled Auto-Connect / Auto-Disconnect
                            SettingToggleRow(
                                title = s.scheduleVpnTitle,
                                subtitle = s.scheduleVpnSub,
                                checked = scheduleEnabled,
                                onCheckedChange = {
                                    scheduleEnabled = it
                                    settingsStore.scheduleEnabled = it
                                },
                            )
                            if (scheduleEnabled) {
                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                                SettingClickableRow(
                                    title = s.scheduleFromLabel,
                                    value = s.language.formatClockTimeLocalized(scheduleStartHour, scheduleStartMinute),
                                    onClick = {
                                        TimePickerDialog(
                                            context,
                                            { _, h, m ->
                                                scheduleStartHour = h
                                                scheduleStartMinute = m
                                                settingsStore.scheduleStartHour = h
                                                settingsStore.scheduleStartMinute = m
                                            },
                                            scheduleStartHour,
                                            scheduleStartMinute,
                                            true,
                                        ).show()
                                    },
                                )
                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                                SettingClickableRow(
                                    title = s.scheduleToLabel,
                                    value = s.language.formatClockTimeLocalized(scheduleEndHour, scheduleEndMinute),
                                    onClick = {
                                        TimePickerDialog(
                                            context,
                                            { _, h, m ->
                                                scheduleEndHour = h
                                                scheduleEndMinute = m
                                                settingsStore.scheduleEndHour = h
                                                settingsStore.scheduleEndMinute = m
                                            },
                                            scheduleEndHour,
                                            scheduleEndMinute,
                                            true,
                                        ).show()
                                    },
                                )
                            }

                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                            // 2. Smart Idle Auto-Disconnect (Battery & Data Saver)
                            SettingToggleRow(
                                title = s.idleDisconnectTitle,
                                subtitle = s.idleDisconnectSub,
                                checked = idleDisconnectEnabled,
                                onCheckedChange = {
                                    idleDisconnectEnabled = it
                                    settingsStore.idleDisconnectEnabled = it
                                },
                            )
                            if (idleDisconnectEnabled) {
                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                                SettingClickableRow(
                                    title = s.idleTimeoutTitle,
                                    value = s.language.formatMinutesLocalized(idleTimeoutMinutes),
                                    onClick = { showIdleTimeoutDialog = true },
                                )
                            }

                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                            // 3. Session Data Limit Auto-Cutoff
                            SettingToggleRow(
                                title = s.sessionDataLimitTitle,
                                subtitle = s.sessionDataLimitSub,
                                checked = sessionDataLimitEnabled,
                                onCheckedChange = {
                                    sessionDataLimitEnabled = it
                                    settingsStore.sessionDataLimitEnabled = it
                                },
                            )
                            if (sessionDataLimitEnabled) {
                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                                SettingClickableRow(
                                    title = s.sessionDataLimitAmountTitle,
                                    value = "${s.language.localizeNumber(sessionDataLimitMb)} MB",
                                    onClick = { showDataCapDialog = true },
                                )
                            }

                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                            // 4. Smart Ping Auto-Optimizer
                            SettingToggleRow(
                                title = s.smartPingSwitchTitle,
                                subtitle = s.smartPingSwitchSub,
                                checked = smartPingAutoSwitch,
                                onCheckedChange = {
                                    smartPingAutoSwitch = it
                                    settingsStore.smartPingAutoSwitch = it
                                },
                            )

                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                            // 5. Auto-Connect on App Launch
                            SettingToggleRow(
                                title = s.autoConnectLaunchTitle,
                                subtitle = s.autoConnectLaunchSub,
                                checked = autoConnectOnLaunch,
                                onCheckedChange = {
                                    autoConnectOnLaunch = it
                                    settingsStore.autoConnectOnLaunch = it
                                },
                            )

                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))

                            // 6. Stealth Lockscreen Notification Privacy
                            SettingToggleRow(
                                title = s.stealthNotificationTitle,
                                subtitle = s.stealthNotificationSub,
                                checked = stealthNotificationEnabled,
                                onCheckedChange = {
                                    stealthNotificationEnabled = it
                                    settingsStore.stealthNotificationEnabled = it
                                },
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 2C. SYSTEM PERMISSIONS & PACKAGE INSTALLER
                    SectionHeading(s.sectionPermissions)
                    NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            val canInstallPackages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                runCatching { context.packageManager.canRequestPackageInstalls() }.getOrDefault(false)
                            } else {
                                true
                            }
                            SettingClickableRow(
                                title = s.installPermissionTitle,
                                value = if (canInstallPackages) s.installPermissionGrantedSub else s.installPermissionRequestSub,
                                onClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        runCatching {
                                            val intent = Intent(
                                                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                                Uri.parse("package:${context.packageName}"),
                                            )
                                            context.startActivity(intent)
                                        }
                                    }
                                },
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 3. CONNECTION
                    SectionHeading(s.sectionConnection)
                    NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            SettingToggleRow(
                                title = s.verifyExitTitle,
                                subtitle = s.verifyExitSub,
                                checked = exitCheckEnabled,
                                onCheckedChange = {
                                    exitCheckEnabled = it
                                    settingsStore.exitCheckEnabled = it
                                },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.tunnelMtuTitle,
                                value = s.mtuBytesValue(mtu),
                                onClick = { showMtuDialog = true },
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 4. DNS
                    SectionHeading(s.sectionDns)
                    NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            SettingClickableRow(
                                title = s.tunnelDnsTitle,
                                value = when (dnsProvider) {
                                    SettingsStore.DnsProvider.CUSTOM -> customDohUrl.ifBlank { dnsProvider.localizedLabel(s.language) }
                                    else -> dnsProvider.localizedLabel(s.language)
                                },
                                onClick = { showDnsProviderDialog = true },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.resolverDnsTitle,
                                value = when (bootstrapDnsProvider) {
                                    SettingsStore.BootstrapDnsProvider.CUSTOM -> s.language.localizeDigits(customBootstrapIp.ifBlank { bootstrapDnsProvider.localizedLabel(s.language) })
                                    else -> bootstrapDnsProvider.localizedLabel(s.language)
                                },
                                onClick = { showBootstrapDnsDialog = true },
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 5. LOCAL PROXY
                    SectionHeading(s.sectionLocalProxy)
                    NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            SettingToggleRow(
                                title = s.proxyOnlyModeTitle,
                                subtitle = s.proxyOnlyModeSub,
                                checked = proxyOnlyMode,
                                onCheckedChange = {
                                    proxyOnlyMode = it
                                    settingsStore.proxyOnlyMode = it
                                },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.localBindAddressTitle,
                                value = s.language.localizeDigits(socksBindAddress),
                                onClick = { showSocksBindDialog = true },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.socksPortTitle,
                                value = s.language.localizeNumber(socksPort),
                                onClick = { showSocksPortDialog = true },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingToggleRow(
                                title = s.enableHttpProxyTitle,
                                subtitle = s.enableHttpProxySub,
                                checked = httpProxyEnabled,
                                onCheckedChange = {
                                    httpProxyEnabled = it
                                    settingsStore.httpProxyEnabled = it
                                },
                            )
                            if (httpProxyEnabled) {
                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                                SettingClickableRow(
                                    title = s.httpProxyPortTitle,
                                    value = s.language.localizeNumber(httpProxyPort),
                                    onClick = { showHttpPortDialog = true },
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 6. SPLIT TUNNELING
                    SectionHeading(s.sectionSplitTunneling)
                    NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            SettingClickableRow(
                                title = s.excludedAppsTitle,
                                value = if (excludedApps.isEmpty()) s.none else s.excludedAppsCount(excludedApps.size),
                                onClick = { showSplitTunnelDialog = true },
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 7. ADVANCED NETWORKING
                    SectionHeading(s.sectionAdvancedNetworking)
                    NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            SettingClickableRow(
                                title = s.customEdgeAddressTitle,
                                value = if (customEdgeAddress.isBlank()) s.notSet else s.language.localizeDigits(customEdgeAddress),
                                onClick = { showEdgeAddressDialog = true },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.tlsFragmentationTitle,
                                value = tlsSplitMode.localizedLabel(s.language),
                                onClick = { showTlsSplitDialog = true },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingToggleRow(
                                title = s.chainUpstreamProxyTitle,
                                subtitle = s.chainUpstreamProxySub,
                                checked = upstreamProxyEnabled,
                                onCheckedChange = {
                                    upstreamProxyEnabled = it
                                    settingsStore.upstreamProxyEnabled = it
                                },
                            )
                            if (upstreamProxyEnabled) {
                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                                SettingClickableRow(
                                    title = s.upstreamProxyTypeTitle,
                                    value = upstreamProxyType.localizedLabel(s.language),
                                    onClick = { showUpstreamProxyTypeDialog = true },
                                )
                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                                SettingClickableRow(
                                    title = s.upstreamProxyAddressTitle,
                                    value = if (upstreamProxyHost.isBlank()) s.notSet else s.language.localizeDigits("$upstreamProxyHost:$upstreamProxyPort"),
                                    onClick = { showUpstreamProxyAddressDialog = true },
                                )
                                HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                                SettingClickableRow(
                                    title = s.upstreamProxyCredentialsTitle,
                                    value = if (upstreamProxyUsername.isBlank()) s.none else upstreamProxyUsername,
                                    onClick = { showUpstreamProxyCredentialsDialog = true },
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // 8. ABOUT, SOCIAL LINKS & LOGS (GitHub removed; Instagram & Telegram added)
                    SectionHeading(s.sectionAboutAndLogs)
                    NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            SettingClickableRow(
                                title = s.viewConnectionLogsTitle,
                                value = s.viewConnectionLogsSub,
                                onClick = onOpenLogs,
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.versionTitle,
                                value = s.language.localizeDigits(BuildConfig.VERSION_NAME),
                                onClick = {},
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.instagramTitle,
                                value = s.instagramSubtitle,
                                onClick = {
                                    runCatching {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM_URL)))
                                    }
                                },
                            )
                            HorizontalDivider(color = neu.textSecondary.copy(alpha = 0.12f), modifier = Modifier.padding(vertical = 12.dp))
                            SettingClickableRow(
                                title = s.telegramTitle,
                                value = s.telegramSubtitle,
                                onClick = {
                                    runCatching {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM_URL)))
                                    }
                                },
                            )
                        }
                    }

                    if (showIdleTimeoutDialog) {
                        val options = listOf(5, 10, 15, 30, 60)
                        AlertDialog(
                            onDismissRequest = { showIdleTimeoutDialog = false },
                            title = { Text(s.idleTimeoutTitle, fontWeight = FontWeight.Bold) },
                            text = {
                                Column {
                                    options.forEach { mins ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    idleTimeoutMinutes = mins
                                                    settingsStore.idleTimeoutMinutes = mins
                                                    showIdleTimeoutDialog = false
                                                }
                                                .padding(vertical = 8.dp),
                                        ) {
                                            RadioButton(
                                                selected = idleTimeoutMinutes == mins,
                                                onClick = {
                                                    idleTimeoutMinutes = mins
                                                    settingsStore.idleTimeoutMinutes = mins
                                                    showIdleTimeoutDialog = false
                                                },
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(s.language.formatMinutesLocalized(mins))
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { showIdleTimeoutDialog = false }) {
                                    Text(s.cancel)
                                }
                            },
                        )
                    }

                    if (showDataCapDialog) {
                        val options = listOf(250, 500, 1024, 2048, 5120)
                        AlertDialog(
                            onDismissRequest = { showDataCapDialog = false },
                            title = { Text(s.sessionDataLimitAmountTitle, fontWeight = FontWeight.Bold) },
                            text = {
                                Column {
                                    options.forEach { mb ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    sessionDataLimitMb = mb
                                                    settingsStore.sessionDataLimitMb = mb
                                                    showDataCapDialog = false
                                                }
                                                .padding(vertical = 8.dp),
                                        ) {
                                            RadioButton(
                                                selected = sessionDataLimitMb == mb,
                                                onClick = {
                                                    sessionDataLimitMb = mb
                                                    settingsStore.sessionDataLimitMb = mb
                                                    showDataCapDialog = false
                                                },
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text("${s.language.localizeNumber(mb)} MB")
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { showDataCapDialog = false }) {
                                    Text(s.cancel)
                                }
                            },
                        )
                    }

                    Spacer(Modifier.height(26.dp))

                    // Sign Out Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.12f))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(18.dp),
                            )
                            .clickable(onClick = onSignOut)
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                s.signOutButton,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = fonts.headingFont,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }

                    Spacer(Modifier.height(36.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionHeading(text: String) {
    val neu = currentNeumorphicColors
    val fonts = currentFoxyFonts
    val s = currentFoxyStrings
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(
            fontFamily = fonts.headingFont,
            fontWeight = FontWeight.Bold,
            letterSpacing = if (s.isRtl) 0.sp else 1.1.sp,
        ),
        color = neu.textSecondary,
        modifier = Modifier.padding(start = 6.dp, bottom = 10.dp, top = 4.dp),
    )
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val neu = currentNeumorphicColors
    val fonts = currentFoxyFonts
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = fonts.headingFont,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = neu.textPrimary,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                color = neu.textSecondary,
                lineHeight = 18.sp,
            )
        }
        Spacer(Modifier.width(16.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = neu.accent,
                uncheckedThumbColor = neu.textSecondary,
                uncheckedTrackColor = neu.surfaceElevated,
            ),
        )
    }
}

@Composable
private fun SettingClickableRow(
    title: String,
    value: String,
    onClick: () -> Unit,
) {
    val neu = currentNeumorphicColors
    val fonts = currentFoxyFonts
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = fonts.headingFont,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = neu.textPrimary,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = fonts.bodyFont),
                color = neu.textSecondary,
                lineHeight = 18.sp,
            )
        }
        Spacer(Modifier.width(12.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = neu.textSecondary,
            modifier = Modifier.size(22.dp),
        )
    }
}

// Dialogs
@Composable
private fun DynamicIslandSizeDialog(
    current: SettingsStore.DynamicIslandSize,
    onDismiss: () -> Unit,
    onConfirm: (SettingsStore.DynamicIslandSize) -> Unit,
) {
    val s = currentFoxyStrings
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.islandSizeTitle) },
        text = {
            Column {
                SettingsStore.DynamicIslandSize.entries.forEach { size ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(selected == size, onClick = { selected = size })
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected == size, onClick = { selected = size })
                        Spacer(Modifier.width(10.dp))
                        Text(size.localizedLabel(s.language), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun DynamicIslandCornerRadiusDialog(
    current: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val s = currentFoxyStrings
    val presets = when (s.language) {
        AppLanguage.FA -> listOf(
            18 to "نرم و طبیعی (۱۸)",
            22 to "متعادل (۲۲)",
            26 to "کپسول استاندارد (۲۶)",
            30 to "کپسول عمیق (۳۰)",
            36 to "کاملاً گرد (۳۶)",
        )
        AppLanguage.AR -> listOf(
            18 to "ناعم وطبيعي (١٨)",
            22 to "متوازن (٢٢)",
            26 to "كبسولة قياسية (٢٦)",
            30 to "كبسولة عميقة (٣٠)",
            36 to "دائري بالكامل (٣٦)",
        )
        AppLanguage.EN -> listOf(
            18 to "Soft Native (18)",
            22 to "Balanced Pill (22)",
            26 to "Standard Capsule (26)",
            30 to "Deep Capsule (30)",
            36 to "Full Stadium (36)",
        )
    }
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.islandCornerRadiusTitle) },
        text = {
            Column {
                presets.forEach { (radius, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(selected == radius, onClick = { selected = radius })
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected == radius, onClick = { selected = radius })
                        Spacer(Modifier.width(10.dp))
                        Text(label, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun DynamicIslandPositionDialog(
    current: SettingsStore.DynamicIslandPosition,
    onDismiss: () -> Unit,
    onConfirm: (SettingsStore.DynamicIslandPosition) -> Unit,
) {
    val s = currentFoxyStrings
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.islandPositionTitle) },
        text = {
            Column {
                SettingsStore.DynamicIslandPosition.entries.forEach { pos ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(selected == pos, onClick = { selected = pos })
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected == pos, onClick = { selected = pos })
                        Spacer(Modifier.width(10.dp))
                        Text(pos.localizedLabel(s.language), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun DynamicIslandThemeDialog(
    current: SettingsStore.DynamicIslandTheme,
    onDismiss: () -> Unit,
    onConfirm: (SettingsStore.DynamicIslandTheme) -> Unit,
) {
    val s = currentFoxyStrings
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.islandThemeTitle) },
        text = {
            Column {
                SettingsStore.DynamicIslandTheme.entries.forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(selected == theme, onClick = { selected = theme })
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected == theme, onClick = { selected = theme })
                        Spacer(Modifier.width(10.dp))
                        Text(theme.localizedLabel(s.language), style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun DnsProviderPickerDialog(
    currentProvider: SettingsStore.DnsProvider,
    currentCustomUrl: String,
    onDismiss: () -> Unit,
    onConfirm: (SettingsStore.DnsProvider, String) -> Unit,
) {
    val s = currentFoxyStrings
    var selectedProvider by remember { mutableStateOf(currentProvider) }
    var urlText by remember { mutableStateOf(currentCustomUrl) }
    val isCustom = selectedProvider == SettingsStore.DnsProvider.CUSTOM
    val isValidCustomUrl = SettingsStore.isValidDohUrl(urlText)
    val canSave = !isCustom || isValidCustomUrl

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.tunnelDnsTitle) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SettingsStore.DnsProvider.entries.forEach { provider ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedProvider == provider,
                                onClick = { selectedProvider = provider },
                            )
                            .padding(vertical = 6.dp),
                    ) {
                        RadioButton(
                            selected = selectedProvider == provider,
                            onClick = { selectedProvider = provider },
                        )
                        Column(Modifier.padding(start = 8.dp)) {
                            Text(provider.localizedLabel(s.language))
                            val detail = provider.dohUrl ?: ""
                            if (detail.isNotEmpty()) {
                                Text(
                                    s.language.localizeDigits(detail),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
                if (isCustom) {
                    OutlinedTextField(
                        value = urlText,
                        onValueChange = { urlText = it.trim() },
                        label = { Text(s.dialogDohUrlLabel) },
                        singleLine = true,
                        isError = urlText.isNotBlank() && !isValidCustomUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedProvider, urlText) },
                enabled = canSave,
            ) {
                Text(s.save)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun BootstrapDnsPickerDialog(
    currentProvider: SettingsStore.BootstrapDnsProvider,
    currentCustomIp: String,
    onDismiss: () -> Unit,
    onConfirm: (SettingsStore.BootstrapDnsProvider, String) -> Unit,
) {
    val s = currentFoxyStrings
    var selectedProvider by remember { mutableStateOf(currentProvider) }
    var ipText by remember { mutableStateOf(currentCustomIp) }
    val isCustom = selectedProvider == SettingsStore.BootstrapDnsProvider.CUSTOM
    val isValidCustomIp = SettingsStore.isValidIpAddress(ipText.trim())
    val canSave = !isCustom || isValidCustomIp

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.resolverDnsTitle) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SettingsStore.BootstrapDnsProvider.entries.forEach { provider ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedProvider == provider,
                                onClick = { selectedProvider = provider },
                            )
                            .padding(vertical = 6.dp),
                    ) {
                        RadioButton(
                            selected = selectedProvider == provider,
                            onClick = { selectedProvider = provider },
                        )
                        Column(Modifier.padding(start = 8.dp)) {
                            Text(provider.localizedLabel(s.language))
                            Text(
                                provider.localizedDescription(s.language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                if (isCustom) {
                    OutlinedTextField(
                        value = ipText,
                        onValueChange = { ipText = it.trim() },
                        label = { Text(s.dialogIpAddressLabel) },
                        singleLine = true,
                        isError = ipText.isNotBlank() && !isValidCustomIp,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedProvider, ipText) },
                enabled = canSave,
            ) {
                Text(s.save)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun SocksBindAddressPickerDialog(
    current: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val s = currentFoxyStrings
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.localBindAddressTitle) },
        text = {
            Column {
                SettingsStore.socksBindPresetsFor(s.language).forEach { (address, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selected == address,
                                onClick = { selected = address },
                            )
                            .padding(vertical = 6.dp),
                    ) {
                        RadioButton(
                            selected = selected == address,
                            onClick = { selected = address },
                        )
                        Text(label, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun SocksPortPickerDialog(
    current: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val s = currentFoxyStrings
    var text by remember { mutableStateOf(current.toString()) }
    val port = text.toIntOrNull()
    val isValid = port != null && port in 1..65_535

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.socksPortTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it.filter(Char::isDigit).take(5) },
                    label = { Text(s.dialogPortRangeLabel) },
                    singleLine = true,
                    isError = text.isNotBlank() && !isValid,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )
                TextButton(
                    onClick = { text = SettingsStore.DEFAULT_SOCKS_PORT.toString() },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(s.resetToDefault(SettingsStore.DEFAULT_SOCKS_PORT.toString()))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { port?.let { onConfirm(it) } }, enabled = isValid) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun HttpPortPickerDialog(
    current: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val s = currentFoxyStrings
    var text by remember { mutableStateOf(current.toString()) }
    val port = text.toIntOrNull()
    val isValid = port != null && port in 1..65_535

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.httpProxyPortTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it.filter(Char::isDigit).take(5) },
                    label = { Text(s.dialogPortRangeLabel) },
                    singleLine = true,
                    isError = text.isNotBlank() && !isValid,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )
                TextButton(
                    onClick = { text = SettingsStore.DEFAULT_HTTP_PROXY_PORT.toString() },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(s.resetToDefault(SettingsStore.DEFAULT_HTTP_PROXY_PORT.toString()))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { port?.let { onConfirm(it) } }, enabled = isValid) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun EdgeAddressPickerDialog(
    current: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val s = currentFoxyStrings
    var text by remember { mutableStateOf(current) }
    val trimmed = text.trim()
    val isValid = trimmed.isEmpty() || SettingsStore.isValidEdgeHost(trimmed)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.customEdgeAddressTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it.trim() },
                    label = { Text(s.dialogHostOrIpLabel) },
                    singleLine = true,
                    isError = text.isNotBlank() && !isValid,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(trimmed) }, enabled = isValid) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun UpstreamProxyTypeDialog(
    current: SettingsStore.UpstreamProxyType,
    onDismiss: () -> Unit,
    onConfirm: (SettingsStore.UpstreamProxyType) -> Unit,
) {
    val s = currentFoxyStrings
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.upstreamProxyTypeTitle) },
        text = {
            Column {
                SettingsStore.UpstreamProxyType.entries.forEach { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selected == type,
                                onClick = { selected = type },
                            )
                            .padding(vertical = 6.dp),
                    ) {
                        RadioButton(selected = selected == type, onClick = { selected = type })
                        Text(type.localizedLabel(s.language), modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun UpstreamProxyAddressDialog(
    currentHost: String,
    currentPort: Int,
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit,
) {
    val s = currentFoxyStrings
    var hostText by remember { mutableStateOf(currentHost) }
    var portText by remember { mutableStateOf(currentPort.toString()) }
    val port = portText.toIntOrNull()
    val isValidHost = hostText.isBlank() || SettingsStore.isValidEdgeHost(hostText)
    val isValidPort = port != null && port in 1..65_535
    val canSave = isValidHost && isValidPort

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.upstreamProxyAddressTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = hostText,
                    onValueChange = { hostText = it.trim() },
                    label = { Text(s.dialogHostOrIpLabel) },
                    singleLine = true,
                    isError = hostText.isNotBlank() && !isValidHost,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = portText,
                    onValueChange = { portText = it.filter(Char::isDigit).take(5) },
                    label = { Text(s.dialogPortRangeLabel) },
                    singleLine = true,
                    isError = portText.isNotBlank() && !isValidPort,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { port?.let { onConfirm(hostText, it) } },
                enabled = canSave,
            ) {
                Text(s.save)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun UpstreamProxyCredentialsDialog(
    currentUsername: String,
    currentPassword: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    val s = currentFoxyStrings
    var username by remember { mutableStateOf(currentUsername) }
    var password by remember { mutableStateOf(currentPassword) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.upstreamProxyCredentialsTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(s.dialogUsernameLabel) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(s.loginPasswordLabel) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(username, password) }) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun TlsSplitModePickerDialog(
    current: SettingsStore.TlsSplitMode,
    onDismiss: () -> Unit,
    onConfirm: (SettingsStore.TlsSplitMode) -> Unit,
) {
    val s = currentFoxyStrings
    var selected by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.tlsFragmentationTitle) },
        text = {
            Column {
                SettingsStore.TlsSplitMode.entries.forEach { mode ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selected == mode,
                                onClick = { selected = mode },
                            )
                            .padding(vertical = 6.dp),
                    ) {
                        RadioButton(selected = selected == mode, onClick = { selected = mode })
                        Column(Modifier.padding(start = 8.dp)) {
                            Text(mode.localizedLabel(s.language))
                            Text(
                                mode.localizedDescription(s.language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

private data class AppItem(
    val packageName: String,
    val label: String,
)

@Composable
private fun SplitTunnelDialog(
    current: Set<String>,
    onDismiss: () -> Unit,
    onConfirm: (Set<String>) -> Unit,
) {
    val context = LocalContext.current
    val s = currentFoxyStrings
    var selected by remember { mutableStateOf(current) }
    var searchQuery by remember { mutableStateOf("") }

    val installedApps by produceState<List<AppItem>>(initialValue = emptyList()) {
        value = withContext(Dispatchers.IO) {
            val pm = context.packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            packages
                .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 || it.packageName == "com.android.chrome" }
                .map { AppItem(it.packageName, pm.getApplicationLabel(it).toString()) }
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
        title = { Text(s.excludedAppsTitle) },
        text = {
            Column(Modifier.height(400.dp)) {
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
                                    Text(app.label, maxLines = 1)
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
                Text("${s.save} (${s.language.localizeNumber(selected.size)})")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}

@Composable
private fun MtuPickerDialog(
    current: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val s = currentFoxyStrings
    var mtuText by remember { mutableStateOf(current.toString()) }
    val parsedMtu = mtuText.toIntOrNull()
    val isValid = parsedMtu != null && parsedMtu in SettingsStore.MIN_MTU..SettingsStore.MAX_MTU

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(s.tunnelMtuTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = mtuText,
                    onValueChange = { mtuText = it.filter(Char::isDigit).take(4) },
                    label = { Text(s.language.localizeDigits("${SettingsStore.MIN_MTU}\u2013${SettingsStore.MAX_MTU}")) },
                    singleLine = true,
                    isError = mtuText.isNotBlank() && !isValid,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )
                TextButton(
                    onClick = { mtuText = SettingsStore.DEFAULT_MTU.toString() },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(s.resetToDefault(SettingsStore.DEFAULT_MTU.toString()))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { parsedMtu?.let { onConfirm(it) } }, enabled = isValid) { Text(s.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(s.cancel) }
        },
    )
}
