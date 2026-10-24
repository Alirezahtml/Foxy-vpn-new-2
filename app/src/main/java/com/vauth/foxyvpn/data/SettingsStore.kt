package com.vauth.foxyvpn.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.vauth.foxyvpn.ui.theme.AppLanguage
import com.vauth.foxyvpn.ui.theme.ThemeMode
import com.vauth.foxyvpn.vpn.FoxyVpnService
import com.vauth.foxyvpn.widget.QuickConnectWidgetProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

enum class FoxyColorTheme(
    val label: String,
    val persianLabel: String,
    val arabicLabel: String,
    val primaryLightHex: Long,
    val primaryDarkHex: Long,
    val secondaryAccentHex: Long,
) {
    CYBER_FOX(
        label = "Cyber Fox Coral",
        persianLabel = "نارنجی مرجانی فاکسی",
        arabicLabel = "مرجاني فوكسي",
        primaryLightHex = 0xFFE05324,
        primaryDarkHex = 0xFFFF7849,
        secondaryAccentHex = 0xFF10B981,
    ),
    OCEAN_SAPPHIRE(
        label = "Ocean Sapphire",
        persianLabel = "آبی اقیانوسی آرام",
        arabicLabel = "ياقوت المحيط الأزرق",
        primaryLightHex = 0xFF0284C7,
        primaryDarkHex = 0xFF38BDF8,
        secondaryAccentHex = 0xFF2DD4BF,
    ),
    EMERALD_AURORA(
        label = "Emerald Aurora",
        persianLabel = "زمرد شفق قطبی",
        arabicLabel = "زمرد الشفق القطبي",
        primaryLightHex = 0xFF059669,
        primaryDarkHex = 0xFF34D399,
        secondaryAccentHex = 0xFF06B6D4,
    ),
    ROYAL_AMETHYST(
        label = "Royal Amethyst",
        persianLabel = "بنفش سلطنتی یاقوتی",
        arabicLabel = "جمشت ملكي بنفسجي",
        primaryLightHex = 0xFF7C3AED,
        primaryDarkHex = 0xFFA78BFA,
        secondaryAccentHex = 0xFFEC4899,
    ),
    ROSE_VELVET(
        label = "Rose Velvet",
        persianLabel = "رز گلد مخملی",
        arabicLabel = "وردي مخملي",
        primaryLightHex = 0xFFE11D48,
        primaryDarkHex = 0xFFFB7185,
        secondaryAccentHex = 0xFFF59E0B,
    ),
    SOLAR_GOLD(
        label = "Solar Amber Gold",
        persianLabel = "طلایی کهربایی مدرن",
        arabicLabel = "ذهبي كهرماني",
        primaryLightHex = 0xFFD97706,
        primaryDarkHex = 0xFFFBBF24,
        secondaryAccentHex = 0xFF14B8A6,
    ),
    ;

    fun localizedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> label
        AppLanguage.FA -> persianLabel
        AppLanguage.AR -> arabicLabel
    }
}

enum class DynamicIslandLedStyle(
    val label: String,
    val persianLabel: String,
    val arabicLabel: String,
) {
    CYBER_RAINBOW("Automotive RGB Chase", "ال‌ای‌دی نئون خودرو", "إضاءة سيارات آر جي بي"),
    FOXY_FIRE("Foxy Fire Turbo", "توربو آتشین فاکسی", "توربو ناري فوكسي"),
    EMERALD_PULSE("Emerald Matrix Wave", "موج زمردی متحرک", "موجة زمردية متحركة"),
    POLICE_STROBE("Police Strobe Red/Blue", "چراغ پلیسی قرمز و آبی", "إضاءة شرطة أحمر وأزرق"),
    SOLAR_GOLD("Sport Gold & Crimson", "اسپرت طلایی و قرمز", "ذهبي وأحمر رياضي"),
    AURORA_BOREALIS("Aurora Borealis Silk", "شفق قطبی ابریشمی", "الشفق القطبي الحريري"),
    OCEAN_BIOLUME("Ocean Bioluminescence", "درخشش اعماق اقیانوس", "توهج أعماق المحيط"),
    ROYAL_NEBULA("Galaxy Purple Nebula", "سحابی بنفش کهکشانی", "سديم المجرة البنفسجي"),
    CYBERPUNK_SYNTH("Cyberpunk Synthwave", "نئون سایبرپانک", "نيون سايبر بانك"),
    LIQUID_MERCURY("Liquid Chrome Silver", "نقره‌ای جیوه مایع", "فضة الكروم السائل"),
    ARCTIC_FROST("Arctic Ice Crystal", "کریستال یخی قطبی", "كريستال الجليد القطبي"),
    SUNSET_MIAMI("Miami Sunset Horizon", "افق غروب میامی", "غروب ميامي المتدرج"),
    QUANTUM_PLASMA("Quantum Plasma Beam", "پرتو پلاسمای کوانتومی", "شعاع البلازما الكمومي"),
    SAKURA_BLOSSOM("Sakura Rose Glow", "درخشش شکوفه ساکورا", "توهج زهور الساكورا"),
    ;

    fun localizedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> label
        AppLanguage.FA -> persianLabel
        AppLanguage.AR -> arabicLabel
    }
}

enum class DynamicIslandMotionStyle(
    val label: String,
    val persianLabel: String,
    val arabicLabel: String,
) {
    LIQUID_SPRING("Liquid Silk 120Hz", "فیزیک مایع ابریشمی ۱۲۰ هرتز", "فيزياء الحرير السائل ١٢٠ هرتز"),
    ZERO_GRAVITY("Zero-Gravity Float", "شناور بی‌وزنی فضایی", "طفو انعدام الجاذبية"),
    BREATHING_HALO("Breathing Aura Halo", "تنفس نوری آرامش‌بخش", "توهج التنفس الهادئ"),
    SONIC_WAVE_SYNC("Sonic Wave Sync", "موج صوتی هماهنگ با ترافیک", "موجة صوتية متزامنة"),
    HEARTBEAT_PULSE("Live Heartbeat Pulse", "ضربان قلب زنده", "نبض القلب الحي"),
    PRISM_SHIMMER("Crystal Prism Shimmer", "درخشش منشوری کریستال", "بريق المنشور الكريستالي"),
    MAGNETIC_ELASTIC("Magnetic Elastic Morph", "کشسانی مغناطیسی نرم", "مرونة مغناطيسية ناعمة"),
    STARLIGHT_ORBIT("Starlight Orbital", "مدار ستاره‌ای درخشان", "مدار النجوم المضيئة"),
    ;

    fun localizedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> label
        AppLanguage.FA -> persianLabel
        AppLanguage.AR -> arabicLabel
    }
}

class SettingsStore(context: Context) {
    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences("foxyvpn_settings", Context.MODE_PRIVATE)
    private val _islandConfigRevision = MutableStateFlow(0)
    val islandConfigRevision: StateFlow<Int> = _islandConfigRevision

    private val credentialsPrefs by lazy {
        createSafeEncryptedPrefs(appContext, "foxyvpn_settings_credentials")
    }

    var appLanguage: AppLanguage
        get() {
            val saved = prefs.getString(KEY_APP_LANGUAGE, null)
            return AppLanguage.entries.firstOrNull { it.name == saved || it.code == saved }
                ?: when (Locale.getDefault().language) {
                    "fa" -> AppLanguage.FA
                    "ar" -> AppLanguage.AR
                    else -> AppLanguage.EN
                }
        }
        set(value) {
            prefs.edit().putString(KEY_APP_LANGUAGE, value.name).apply()
            _islandConfigRevision.value++
        }

    var themeMode: ThemeMode
        get() = ThemeMode.entries.firstOrNull {
            it.name == prefs.getString(KEY_THEME_MODE, null)
        } ?: ThemeMode.SYSTEM
        set(value) {
            prefs.edit().putString(KEY_THEME_MODE, value.name).apply()
            QuickConnectWidgetProvider.updateAll(appContext, FoxyVpnService.state.value)
            _islandConfigRevision.value++
        }

    var appColorTheme: FoxyColorTheme
        get() = FoxyColorTheme.entries.firstOrNull {
            it.name == prefs.getString(KEY_APP_COLOR_THEME, null)
        } ?: FoxyColorTheme.CYBER_FOX
        set(value) {
            prefs.edit().putString(KEY_APP_COLOR_THEME, value.name).apply()
            _islandConfigRevision.value++
        }

    var exitCheckEnabled: Boolean
        get() = prefs.getBoolean(KEY_EXIT_CHECK, true)
        set(value) { prefs.edit().putBoolean(KEY_EXIT_CHECK, value).apply() }

    var mtu: Int
        get() = prefs.getInt(KEY_CUSTOM_MTU, DEFAULT_MTU)
        set(value) { prefs.edit().putInt(KEY_CUSTOM_MTU, value.coerceIn(MIN_MTU, MAX_MTU)).apply() }

    var cachedRealIp: String?
        get() = prefs.getString(KEY_CACHED_REAL_IP, null)
        set(value) { prefs.edit().putString(KEY_CACHED_REAL_IP, value).apply() }

    var cachedRealCountry: String?
        get() = prefs.getString(KEY_CACHED_REAL_COUNTRY, null)
        set(value) { prefs.edit().putString(KEY_CACHED_REAL_COUNTRY, value).apply() }

    var cachedQuotaRemaining: Long?
        get() = if (prefs.contains(KEY_CACHED_QUOTA_REMAINING)) prefs.getLong(KEY_CACHED_QUOTA_REMAINING, -1L).takeIf { it >= 0 } else null
        set(value) {
            if (value != null && value >= 0) prefs.edit().putLong(KEY_CACHED_QUOTA_REMAINING, value).apply()
            else prefs.edit().remove(KEY_CACHED_QUOTA_REMAINING).apply()
        }

    var cachedQuotaMax: Long?
        get() = if (prefs.contains(KEY_CACHED_QUOTA_MAX)) prefs.getLong(KEY_CACHED_QUOTA_MAX, -1L).takeIf { it >= 0 } else null
        set(value) {
            if (value != null && value >= 0) prefs.edit().putLong(KEY_CACHED_QUOTA_MAX, value).apply()
            else prefs.edit().remove(KEY_CACHED_QUOTA_MAX).apply()
        }

    var cachedIsUnlimited: Boolean
        get() = prefs.getBoolean(KEY_CACHED_IS_UNLIMITED, false)
        set(value) { prefs.edit().putBoolean(KEY_CACHED_IS_UNLIMITED, value).apply() }

    fun clearAccountSession() {
        prefs.edit()
            .remove(KEY_CACHED_REAL_IP)
            .remove(KEY_CACHED_REAL_COUNTRY)
            .remove(KEY_CACHED_QUOTA_REMAINING)
            .remove(KEY_CACHED_QUOTA_MAX)
            .remove(KEY_CACHED_IS_UNLIMITED)
            .apply()
    }

    var socksBindAddress: String
        get() = prefs.getString(KEY_SOCKS_BIND_ADDRESS, DEFAULT_SOCKS_BIND_ADDRESS) ?: DEFAULT_SOCKS_BIND_ADDRESS
        set(value) {
            prefs.edit().putString(KEY_SOCKS_BIND_ADDRESS, value.ifBlank { DEFAULT_SOCKS_BIND_ADDRESS }).apply()
        }

    var socksPort: Int
        get() = prefs.getInt(KEY_SOCKS_PORT, DEFAULT_SOCKS_PORT)
        set(value) { prefs.edit().putInt(KEY_SOCKS_PORT, value.coerceIn(1, 65_535)).apply() }

    var httpProxyEnabled: Boolean
        get() = prefs.getBoolean(KEY_HTTP_PROXY_ENABLED, false)
        set(value) { prefs.edit().putBoolean(KEY_HTTP_PROXY_ENABLED, value).apply() }

    var httpProxyPort: Int
        get() = prefs.getInt(KEY_HTTP_PROXY_PORT, DEFAULT_HTTP_PROXY_PORT)
        set(value) { prefs.edit().putInt(KEY_HTTP_PROXY_PORT, value.coerceIn(1, 65_535)).apply() }

    var dnsProvider: DnsProvider
        get() = DnsProvider.entries.firstOrNull {
            it.name == prefs.getString(KEY_DNS_PROVIDER, null)
        } ?: DnsProvider.SYSTEM_DEFAULT
        set(value) { prefs.edit().putString(KEY_DNS_PROVIDER, value.name).apply() }

    var customDohUrl: String
        get() = prefs.getString(KEY_CUSTOM_DOH_URL, "") ?: ""
        set(value) {
            val trimmed = value.trim()
            if (trimmed.isNotEmpty() && !isValidDohUrl(trimmed)) return
            prefs.edit().putString(KEY_CUSTOM_DOH_URL, trimmed).apply()
        }

    val effectiveDohUrl: String?
        get() = when (dnsProvider) {
            DnsProvider.SYSTEM_DEFAULT -> null
            DnsProvider.CUSTOM -> customDohUrl.takeIf { isValidDohUrl(it) }
            else -> dnsProvider.dohUrl
        }

    var bootstrapDnsProvider: BootstrapDnsProvider
        get() = BootstrapDnsProvider.entries.firstOrNull {
            it.name == prefs.getString(KEY_BOOTSTRAP_DNS_PROVIDER, null)
        } ?: BootstrapDnsProvider.AUTOMATIC
        set(value) { prefs.edit().putString(KEY_BOOTSTRAP_DNS_PROVIDER, value.name).apply() }

    var customBootstrapIp: String
        get() = prefs.getString(KEY_CUSTOM_BOOTSTRAP_IP, "") ?: ""
        set(value) {
            val trimmed = value.trim()
            if (trimmed.isNotEmpty() && !isValidIpAddress(trimmed)) return
            prefs.edit().putString(KEY_CUSTOM_BOOTSTRAP_IP, trimmed).apply()
        }

    val effectiveBootstrapAddresses: List<String>
        get() = when (bootstrapDnsProvider) {
            BootstrapDnsProvider.CUSTOM -> {
                val ip = customBootstrapIp.trim()
                if (isValidIpAddress(ip)) listOf(ip) else BootstrapDnsProvider.AUTOMATIC.addresses
            }
            BootstrapDnsProvider.SYSTEM -> emptyList()
            else -> bootstrapDnsProvider.addresses
        }

    var proxyOnlyMode: Boolean
        get() = prefs.getBoolean(KEY_PROXY_ONLY_MODE, false)
        set(value) { prefs.edit().putBoolean(KEY_PROXY_ONLY_MODE, value).apply() }

    var customEdgeAddress: String
        get() = prefs.getString(KEY_CUSTOM_EDGE_ADDRESS, "") ?: ""
        set(value) {
            val normalized = value.trim().lowercase()
            if (normalized.isNotEmpty() && !isValidEdgeHost(normalized)) return
            prefs.edit().putString(KEY_CUSTOM_EDGE_ADDRESS, normalized).apply()
        }

    val effectiveCustomEdgeAddress: String?
        get() = customEdgeAddress.takeIf { isValidEdgeHost(it) }

    var upstreamProxyEnabled: Boolean
        get() = prefs.getBoolean(KEY_UPSTREAM_PROXY_ENABLED, false)
        set(value) { prefs.edit().putBoolean(KEY_UPSTREAM_PROXY_ENABLED, value).apply() }

    var upstreamProxyType: UpstreamProxyType
        get() = UpstreamProxyType.entries.firstOrNull {
            it.name == prefs.getString(KEY_UPSTREAM_PROXY_TYPE, null)
        } ?: UpstreamProxyType.SOCKS5
        set(value) { prefs.edit().putString(KEY_UPSTREAM_PROXY_TYPE, value.name).apply() }

    var upstreamProxyHost: String
        get() = prefs.getString(KEY_UPSTREAM_PROXY_HOST, "") ?: ""
        set(value) { prefs.edit().putString(KEY_UPSTREAM_PROXY_HOST, value.trim()).apply() }

    var upstreamProxyPort: Int
        get() = prefs.getInt(KEY_UPSTREAM_PROXY_PORT, DEFAULT_UPSTREAM_PROXY_PORT)
        set(value) { prefs.edit().putInt(KEY_UPSTREAM_PROXY_PORT, value.coerceIn(1, 65_535)).apply() }

    var upstreamProxyUsername: String
        get() = credentialsPrefs.getString(KEY_UPSTREAM_PROXY_USERNAME, "") ?: ""
        set(value) { credentialsPrefs.edit().putString(KEY_UPSTREAM_PROXY_USERNAME, value).apply() }

    var upstreamProxyPassword: String
        get() = credentialsPrefs.getString(KEY_UPSTREAM_PROXY_PASSWORD, "") ?: ""
        set(value) { credentialsPrefs.edit().putString(KEY_UPSTREAM_PROXY_PASSWORD, value).apply() }

    var excludedApps: Set<String>
        get() = prefs.getStringSet(KEY_EXCLUDED_APPS, emptySet())?.toSet() ?: emptySet()
        set(value) {
            prefs.edit().putStringSet(KEY_EXCLUDED_APPS, HashSet(value)).apply()
        }

    var tlsSplitMode: TlsSplitMode
        get() = TlsSplitMode.entries.firstOrNull {
            it.name == prefs.getString(KEY_TLS_SPLIT_MODE, null)
        } ?: TlsSplitMode.OFF
        set(value) { prefs.edit().putString(KEY_TLS_SPLIT_MODE, value.name).apply() }

    var dynamicIslandEnabled: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_ENABLED, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_ENABLED, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandOverlayEnabled: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_OVERLAY_ENABLED, false)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_OVERLAY_ENABLED, value).apply()
            _islandConfigRevision.value++
        }

    /**
     * Manual 0% to 100% linear size slider for Dynamic Island.
     */
    var dynamicIslandScalePercent: Int
        get() = prefs.getInt(KEY_DYNAMIC_ISLAND_SCALE_PERCENT, 55).coerceIn(0, 100)
        set(value) {
            val clamped = value.coerceIn(0, 100)
            prefs.edit().putInt(KEY_DYNAMIC_ISLAND_SCALE_PERCENT, clamped).apply()
            // Sync preset enum for backwards compatibility
            val preset = when {
                clamped < 34 -> DynamicIslandSize.COMPACT
                clamped < 68 -> DynamicIslandSize.REGULAR
                else -> DynamicIslandSize.EXPANDED
            }
            prefs.edit().putString(KEY_DYNAMIC_ISLAND_SIZE, preset.name).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandSize: DynamicIslandSize
        get() = DynamicIslandSize.entries.firstOrNull {
            it.name == prefs.getString(KEY_DYNAMIC_ISLAND_SIZE, null)
        } ?: DynamicIslandSize.REGULAR
        set(value) {
            prefs.edit().putString(KEY_DYNAMIC_ISLAND_SIZE, value.name).apply()
            val mappedPercent = when (value) {
                DynamicIslandSize.COMPACT -> 20
                DynamicIslandSize.REGULAR -> 55
                DynamicIslandSize.EXPANDED -> 90
            }
            prefs.edit().putInt(KEY_DYNAMIC_ISLAND_SCALE_PERCENT, mappedPercent).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandCornerRadius: Int
        get() = prefs.getInt(KEY_DYNAMIC_ISLAND_CORNER_RADIUS, 28)
        set(value) {
            prefs.edit().putInt(KEY_DYNAMIC_ISLAND_CORNER_RADIUS, value.coerceIn(14, 38)).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandPosition: DynamicIslandPosition
        get() = DynamicIslandPosition.entries.firstOrNull {
            it.name == prefs.getString(KEY_DYNAMIC_ISLAND_POSITION, null)
        } ?: DynamicIslandPosition.TOP_CENTER
        set(value) {
            prefs.edit().putString(KEY_DYNAMIC_ISLAND_POSITION, value.name).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandTheme: DynamicIslandTheme
        get() = DynamicIslandTheme.entries.firstOrNull {
            it.name == prefs.getString(KEY_DYNAMIC_ISLAND_THEME, null)
        } ?: DynamicIslandTheme.DARK
        set(value) {
            prefs.edit().putString(KEY_DYNAMIC_ISLAND_THEME, value.name).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandShowSpeed: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_SHOW_SPEED, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_SHOW_SPEED, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandShowPing: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_SHOW_PING, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_SHOW_PING, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandShowFlag: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_SHOW_FLAG, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_SHOW_FLAG, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandShowTimer: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_SHOW_TIMER, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_SHOW_TIMER, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandAnimation: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_ANIMATION, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_ANIMATION, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandPinnedApps: Set<String>
        get() = prefs.getStringSet(KEY_DYNAMIC_ISLAND_PINNED_APPS, null)?.toSet() ?: DEFAULT_PINNED_ISLAND_APPS
        set(value) {
            prefs.edit().putStringSet(KEY_DYNAMIC_ISLAND_PINNED_APPS, HashSet(value)).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandLedBorderEnabled: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_LED_BORDER, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_LED_BORDER, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandLedStyle: DynamicIslandLedStyle
        get() = DynamicIslandLedStyle.entries.firstOrNull {
            it.name == prefs.getString(KEY_DYNAMIC_ISLAND_LED_STYLE, null)
        } ?: DynamicIslandLedStyle.CYBER_RAINBOW
        set(value) {
            prefs.edit().putString(KEY_DYNAMIC_ISLAND_LED_STYLE, value.name).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandLedSpeed: Float
        get() = runCatching { prefs.getFloat(KEY_DYNAMIC_ISLAND_LED_SPEED, 1.0f) }.getOrDefault(1.0f).coerceIn(0.3f, 2.5f)
        set(value) {
            prefs.edit().putFloat(KEY_DYNAMIC_ISLAND_LED_SPEED, value.coerceIn(0.3f, 2.5f)).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandShowTowerWave: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_SHOW_TOWER_WAVE, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_SHOW_TOWER_WAVE, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandShowServerName: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_SHOW_SERVER_NAME, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_SHOW_SERVER_NAME, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandShowSessionData: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_SHOW_SESSION_DATA, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_SHOW_SESSION_DATA, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandShowIpBadge: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_SHOW_IP_BADGE, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_SHOW_IP_BADGE, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandDraggable: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_ISLAND_DRAGGABLE, true)
        set(value) {
            prefs.edit().putBoolean(KEY_DYNAMIC_ISLAND_DRAGGABLE, value).apply()
            _islandConfigRevision.value++
        }

    var dynamicIslandOffsetX: Float
        get() = runCatching { prefs.getFloat(KEY_DYNAMIC_ISLAND_OFFSET_X, 0f) }.getOrDefault(0f)
        set(value) {
            prefs.edit().putFloat(KEY_DYNAMIC_ISLAND_OFFSET_X, value).apply()
        }

    var dynamicIslandOffsetY: Float
        get() = runCatching { prefs.getFloat(KEY_DYNAMIC_ISLAND_OFFSET_Y, 0f) }.getOrDefault(0f)
        set(value) {
            prefs.edit().putFloat(KEY_DYNAMIC_ISLAND_OFFSET_Y, value).apply()
        }

    fun resetDynamicIslandPosition() {
        prefs.edit()
            .putFloat(KEY_DYNAMIC_ISLAND_OFFSET_X, 0f)
            .putFloat(KEY_DYNAMIC_ISLAND_OFFSET_Y, 0f)
            .apply()
        _islandConfigRevision.value++
    }

    // Scheduled Auto-Connect / Auto-Disconnect Timer
    var scheduleEnabled: Boolean
        get() = prefs.getBoolean(KEY_SCHEDULE_ENABLED, false)
        set(value) {
            prefs.edit().putBoolean(KEY_SCHEDULE_ENABLED, value).apply()
        }

    var scheduleStartHour: Int
        get() = prefs.getInt(KEY_SCHEDULE_START_HOUR, 22).coerceIn(0, 23)
        set(value) {
            prefs.edit().putInt(KEY_SCHEDULE_START_HOUR, value.coerceIn(0, 23)).apply()
        }

    var scheduleStartMinute: Int
        get() = prefs.getInt(KEY_SCHEDULE_START_MINUTE, 0).coerceIn(0, 59)
        set(value) {
            prefs.edit().putInt(KEY_SCHEDULE_START_MINUTE, value.coerceIn(0, 59)).apply()
        }

    var scheduleEndHour: Int
        get() = prefs.getInt(KEY_SCHEDULE_END_HOUR, 7).coerceIn(0, 23)
        set(value) {
            prefs.edit().putInt(KEY_SCHEDULE_END_HOUR, value.coerceIn(0, 23)).apply()
        }

    var scheduleEndMinute: Int
        get() = prefs.getInt(KEY_SCHEDULE_END_MINUTE, 0).coerceIn(0, 59)
        set(value) {
            prefs.edit().putInt(KEY_SCHEDULE_END_MINUTE, value.coerceIn(0, 59)).apply()
        }

    // Smart Idle Auto-Disconnect (Battery & Data Saver)
    var idleDisconnectEnabled: Boolean
        get() = prefs.getBoolean(KEY_IDLE_DISCONNECT_ENABLED, false)
        set(value) {
            prefs.edit().putBoolean(KEY_IDLE_DISCONNECT_ENABLED, value).apply()
        }

    var idleTimeoutMinutes: Int
        get() = prefs.getInt(KEY_IDLE_TIMEOUT_MINUTES, 15).coerceIn(1, 180)
        set(value) {
            prefs.edit().putInt(KEY_IDLE_TIMEOUT_MINUTES, value.coerceIn(1, 180)).apply()
        }

    // Additional Smart Practical Options
    var sessionDataLimitEnabled: Boolean
        get() = prefs.getBoolean(KEY_SESSION_DATA_LIMIT_ENABLED, false)
        set(value) {
            prefs.edit().putBoolean(KEY_SESSION_DATA_LIMIT_ENABLED, value).apply()
        }

    var sessionDataLimitMb: Int
        get() = prefs.getInt(KEY_SESSION_DATA_LIMIT_MB, 1024).coerceIn(50, 50_000)
        set(value) {
            prefs.edit().putInt(KEY_SESSION_DATA_LIMIT_MB, value.coerceIn(50, 50_000)).apply()
        }

    var smartPingAutoSwitch: Boolean
        get() = prefs.getBoolean(KEY_SMART_PING_AUTO_SWITCH, true)
        set(value) {
            prefs.edit().putBoolean(KEY_SMART_PING_AUTO_SWITCH, value).apply()
        }

    var autoConnectOnLaunch: Boolean
        get() = prefs.getBoolean(KEY_AUTO_CONNECT_ON_LAUNCH, false)
        set(value) {
            prefs.edit().putBoolean(KEY_AUTO_CONNECT_ON_LAUNCH, value).apply()
        }

    var stealthNotificationEnabled: Boolean
        get() = prefs.getBoolean(KEY_STEALTH_NOTIFICATION_ENABLED, false)
        set(value) {
            prefs.edit().putBoolean(KEY_STEALTH_NOTIFICATION_ENABLED, value).apply()
        }

    var keepAliveEnabled: Boolean
        get() = prefs.getBoolean(KEY_KEEP_ALIVE_ENABLED, true)
        set(value) {
            prefs.edit().putBoolean(KEY_KEEP_ALIVE_ENABLED, value).apply()
        }

    var keepAliveIntervalSec: Int
        get() = prefs.getInt(KEY_KEEP_ALIVE_INTERVAL_SEC, 20).coerceIn(10, 120)
        set(value) {
            prefs.edit().putInt(KEY_KEEP_ALIVE_INTERVAL_SEC, value.coerceIn(10, 120)).apply()
        }

    var autoReconnectEnabled: Boolean
        get() = prefs.getBoolean(KEY_AUTO_RECONNECT_ENABLED, true)
        set(value) {
            prefs.edit().putBoolean(KEY_AUTO_RECONNECT_ENABLED, value).apply()
        }

    var dynamicIslandMotionStyle: DynamicIslandMotionStyle
        get() = DynamicIslandMotionStyle.entries.firstOrNull {
            it.name == prefs.getString(KEY_DYNAMIC_ISLAND_MOTION_STYLE, null)
        } ?: DynamicIslandMotionStyle.LIQUID_SPRING
        set(value) {
            prefs.edit().putString(KEY_DYNAMIC_ISLAND_MOTION_STYLE, value.name).apply()
            _islandConfigRevision.value++
        }

    /**
     * Unmetered Tunnel Speed Booster: Marks the Android VPN network interface as unmetered
     * via VpnService.Builder.setMetered(false) (Android 10+) so the OS and apps do not apply
     * metered cellular data throttling or background socket restrictions.
     */
    var unmeteredVpnModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_UNMETERED_VPN_MODE, true)
        set(value) {
            prefs.edit().putBoolean(KEY_UNMETERED_VPN_MODE, value).apply()
            _islandConfigRevision.value++
        }

    /**
     * High-Throughput TCP/UDP Socket Buffer Booster: Expands hev-socks5-tunnel TCP buffer window
     * to 256 KB and UDP receive buffer to 1 MB with 32 copy buffers for faster multi-stream throughput.
     */
    var tcpBufferBoostEnabled: Boolean
        get() = prefs.getBoolean(KEY_TCP_BUFFER_BOOST, true)
        set(value) {
            prefs.edit().putBoolean(KEY_TCP_BUFFER_BOOST, value).apply()
            _islandConfigRevision.value++
        }

    /**
     * Smart Battery Saver / Eco Mode: Adaptively throttles background telemetry & ping polling
     * when traffic is idle or screen is off, releases continuous high-drain Wi-Fi locks,
     * and optimizes shader frame pacing.
     */
    var batterySaverModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_BATTERY_SAVER_MODE, false)
        set(value) {
            prefs.edit().putBoolean(KEY_BATTERY_SAVER_MODE, value).apply()
            _islandConfigRevision.value++
        }


    enum class DynamicIslandSize(
        val label: String,
        val persianLabel: String,
        val arabicLabel: String,
        val heightDp: Int,
        val widthDp: Int,
    ) {
        COMPACT("Compact", "کوچک", "مصغّر", 36, 170),
        REGULAR("Regular", "متوسط", "قياسي", 44, 230),
        EXPANDED("Large", "بزرگ", "كبير", 52, 280),
        ;

        fun localizedLabel(isRtl: Boolean): String = if (isRtl) persianLabel else label
        fun localizedLabel(lang: AppLanguage): String = when (lang) {
            AppLanguage.EN -> label
            AppLanguage.FA -> persianLabel
            AppLanguage.AR -> arabicLabel
        }
    }

    enum class DynamicIslandPosition(
        val label: String,
        val persianLabel: String,
        val arabicLabel: String,
    ) {
        TOP_CENTER("Top Center", "بالا وسط", "أعلى الوسط"),
        TOP_START("Top Start", "بالا ابتدا", "أعلى البداية"),
        TOP_END("Top End", "بالا انتها", "أعلى النهاية"),
        ;

        fun localizedLabel(isRtl: Boolean): String = if (isRtl) persianLabel else label
        fun localizedLabel(lang: AppLanguage): String = when (lang) {
            AppLanguage.EN -> label
            AppLanguage.FA -> persianLabel
            AppLanguage.AR -> arabicLabel
        }
    }

    enum class DynamicIslandTheme(
        val label: String,
        val persianLabel: String,
        val arabicLabel: String,
    ) {
        DARK("Native Deep Glass", "شیشه‌ای تیره نیتیو", "زجاجي داكن أصلي"),
        NEUMORPHIC("Soft Translucent", "نیمه‌شفاف نرم", "شفاف ناعم"),
        ACCENT("Brand Accent Glow", "درخشش رنگ برند", "توهج لون العلامة"),
        ;

        fun localizedLabel(isRtl: Boolean): String = if (isRtl) persianLabel else label
        fun localizedLabel(lang: AppLanguage): String = when (lang) {
            AppLanguage.EN -> label
            AppLanguage.FA -> persianLabel
            AppLanguage.AR -> arabicLabel
        }
    }

    enum class TlsSplitMode(
        val label: String,
        val persianLabel: String,
        val arabicLabel: String,
        val description: String,
        val persianDescription: String,
        val arabicDescription: String,
    ) {
        OFF(
            label = "Disabled",
            persianLabel = "غیرفعال",
            arabicLabel = "معطّل",
            description = "Standard TLS handshake without fragmentation",
            persianDescription = "ارتباط استاندارد بدون تکه‌تکه‌سازی بسته",
            arabicDescription = "اتصال قياسي بدون تجزئة الحزم",
        ),
        FIRST_BYTE(
            label = "First Byte",
            persianLabel = "بایت اول",
            arabicLabel = "البايت الأول",
            description = "Splits TLS record header at byte 0",
            persianDescription = "برش هدر بسته در بایت اول برای عبور از فیلترینگ",
            arabicDescription = "تقسيم ترويسة الحزمة عند البايت الأول لتجاوز الحجب",
        ),
        SNI(
            label = "Domain Fragment",
            persianLabel = "تکه‌تکه‌سازی دامنه",
            arabicLabel = "تجزئة اسم النطاق",
            description = "Fragments the hostname extension across packets",
            persianDescription = "تکه‌تکه‌سازی نام دامنه برای دور زدن بازرسی عمیق بسته",
            arabicDescription = "تجزئة امتداد اسم النطاق عبر الحزم لتجاوز الفحص العميق",
        ),
        ;

        fun localizedLabel(isRtl: Boolean): String = if (isRtl) persianLabel else label
        fun localizedLabel(lang: AppLanguage): String = when (lang) {
            AppLanguage.EN -> label
            AppLanguage.FA -> persianLabel
            AppLanguage.AR -> arabicLabel
        }

        fun localizedDescription(isRtl: Boolean): String = if (isRtl) persianDescription else description
        fun localizedDescription(lang: AppLanguage): String = when (lang) {
            AppLanguage.EN -> description
            AppLanguage.FA -> persianDescription
            AppLanguage.AR -> arabicDescription
        }
    }

    enum class UpstreamProxyType(
        val enLabel: String,
        val faLabel: String,
        val arLabel: String,
    ) {
        SOCKS5("SOCKS5", "پراکسی ساکس ۵", "وكيل سوكس ٥"),
        HTTP("HTTP", "پراکسی وب", "وكيل ويب"),
        ;

        fun localizedLabel(lang: AppLanguage): String = when (lang) {
            AppLanguage.EN -> enLabel
            AppLanguage.FA -> faLabel
            AppLanguage.AR -> arLabel
        }
    }

    enum class DnsProvider(
        val label: String,
        val persianLabel: String,
        val arabicLabel: String,
        val dohUrl: String?,
    ) {
        SYSTEM_DEFAULT(
            label = "Default",
            persianLabel = "پیش‌فرض سرور",
            arabicLabel = "افتراضي الخادم",
            dohUrl = null,
        ),
        CLOUDFLARE(
            label = "Cloudflare",
            persianLabel = "کلودفلر",
            arabicLabel = "كلاود فلير",
            dohUrl = "https://1.1.1.1/dns-query",
        ),
        GOOGLE(
            label = "Google",
            persianLabel = "گوگل",
            arabicLabel = "جوجل",
            dohUrl = "https://8.8.8.8/dns-query",
        ),
        QUAD9(
            label = "Quad9",
            persianLabel = "کواد۹",
            arabicLabel = "كواد ٩",
            dohUrl = "https://9.9.9.9/dns-query",
        ),
        ADGUARD(
            label = "AdGuard",
            persianLabel = "ادگارد",
            arabicLabel = "أدجارد",
            dohUrl = "https://dns.adguard-dns.com/dns-query",
        ),
        CUSTOM(
            label = "Custom DoH",
            persianLabel = "دی‌ان‌اس امن سفارشی",
            arabicLabel = "نظام أسماء مشفّر مخصص",
            dohUrl = null,
        ),
        ;

        fun localizedLabel(isRtl: Boolean): String = if (isRtl) persianLabel else label
        fun localizedLabel(lang: AppLanguage): String = when (lang) {
            AppLanguage.EN -> label
            AppLanguage.FA -> persianLabel
            AppLanguage.AR -> arabicLabel
        }
    }

    enum class BootstrapDnsProvider(
        val label: String,
        val persianLabel: String,
        val arabicLabel: String,
        val description: String,
        val persianDescription: String,
        val arabicDescription: String,
        val addresses: List<String>,
    ) {
        AUTOMATIC(
            label = "Automatic",
            persianLabel = "خودکار (پیشنهادی)",
            arabicLabel = "تلقائي (موصى به)",
            description = "Races all default DoH endpoints",
            persianDescription = "استفاده همزمان از سریع‌ترین سرورهای دی‌ان‌اس",
            arabicDescription = "يختار أسرع خوادم نظام الأسماء تلقائياً",
            addresses = listOf("9.9.9.9", "1.1.1.1", "8.8.8.8"),
        ),
        QUAD9(
            label = "Quad9",
            persianLabel = "کواد۹",
            arabicLabel = "كواد ٩",
            description = "9.9.9.9, 149.112.112.112",
            persianDescription = "۹.۹.۹.۹ ، ۱۴۹.۱۱۲.۱۱۲.۱۱۲",
            arabicDescription = "٩.٩.٩.٩ ، ١٤٩.١١٢.١١٢.١١٢",
            addresses = listOf("9.9.9.9", "149.112.112.112"),
        ),
        CLOUDFLARE(
            label = "Cloudflare",
            persianLabel = "کلودفلر",
            arabicLabel = "كلاود فلير",
            description = "1.1.1.1, 1.0.0.1",
            persianDescription = "۱.۱.۱.۱ ، ۱.۰.۰.۱",
            arabicDescription = "١.١.١.١ ، ١.٠.٠.١",
            addresses = listOf("1.1.1.1", "1.0.0.1"),
        ),
        GOOGLE(
            label = "Google",
            persianLabel = "گوگل",
            arabicLabel = "جوجل",
            description = "8.8.8.8, 8.8.4.4",
            persianDescription = "۸.۸.۸.۸ ، ۸.۸.۴.۴",
            arabicDescription = "٨.٨.٨.٨ ، ٨.٨.٤.٤",
            addresses = listOf("8.8.8.8", "8.8.4.4"),
        ),
        SYSTEM(
            label = "System DNS",
            persianLabel = "دی‌ان‌اس سیستم",
            arabicLabel = "نظام أسماء الجهاز",
            description = "Local network DNS",
            persianDescription = "استفاده از دی‌ان‌اس شبکه فعلی دستگاه",
            arabicDescription = "استخدام نظام الأسماء المحلي للشبكة",
            addresses = emptyList(),
        ),
        CUSTOM(
            label = "Custom IP",
            persianLabel = "آی‌پی سفارشی",
            arabicLabel = "عنوان آي بي مخصص",
            description = "Specify a custom DoH IP resolver address",
            persianDescription = "وارد کردن آدرس آی‌پی دلخواه برای دی‌ان‌اس",
            arabicDescription = "تحديد عنوان آي بي مخصص لمحلل الأسماء",
            addresses = emptyList(),
        ),
        ;

        fun localizedLabel(isRtl: Boolean): String = if (isRtl) persianLabel else label
        fun localizedLabel(lang: AppLanguage): String = when (lang) {
            AppLanguage.EN -> label
            AppLanguage.FA -> persianLabel
            AppLanguage.AR -> arabicLabel
        }

        fun localizedDescription(isRtl: Boolean): String = if (isRtl) persianDescription else description
        fun localizedDescription(lang: AppLanguage): String = when (lang) {
            AppLanguage.EN -> description
            AppLanguage.FA -> persianDescription
            AppLanguage.AR -> arabicDescription
        }
    }

    companion object {
        private const val KEY_APP_LANGUAGE = "app_language"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_EXIT_CHECK = "exit_check_enabled"
        private const val KEY_CUSTOM_MTU = "custom_mtu"
        private const val KEY_CACHED_REAL_IP = "cached_real_ip"
        private const val KEY_CACHED_REAL_COUNTRY = "cached_real_country"
        private const val KEY_CACHED_QUOTA_REMAINING = "cached_quota_remaining"
        private const val KEY_CACHED_QUOTA_MAX = "cached_quota_max"
        private const val KEY_CACHED_IS_UNLIMITED = "cached_is_unlimited"
        const val DEFAULT_MTU = 1400
        const val MIN_MTU = 576
        const val MAX_MTU = 9000
        private const val KEY_SOCKS_BIND_ADDRESS = "socks_bind_address"
        private const val KEY_SOCKS_PORT = "socks_port"
        private const val KEY_HTTP_PROXY_ENABLED = "http_proxy_enabled"
        private const val KEY_HTTP_PROXY_PORT = "http_proxy_port"
        private const val KEY_DNS_PROVIDER = "dns_provider"
        private const val KEY_CUSTOM_DOH_URL = "custom_doh_url"
        private const val KEY_BOOTSTRAP_DNS_PROVIDER = "bootstrap_dns_provider"
        private const val KEY_CUSTOM_BOOTSTRAP_IP = "custom_bootstrap_ip"
        private const val KEY_PROXY_ONLY_MODE = "proxy_only_mode"
        private const val KEY_CUSTOM_EDGE_ADDRESS = "custom_edge_address"
        private const val KEY_UPSTREAM_PROXY_ENABLED = "upstream_proxy_enabled"
        private const val KEY_UPSTREAM_PROXY_TYPE = "upstream_proxy_type"
        private const val KEY_UPSTREAM_PROXY_HOST = "upstream_proxy_host"
        private const val KEY_UPSTREAM_PROXY_PORT = "upstream_proxy_port"
        private const val KEY_UPSTREAM_PROXY_USERNAME = "upstream_proxy_username"
        private const val KEY_UPSTREAM_PROXY_PASSWORD = "upstream_proxy_password"
        private const val KEY_EXCLUDED_APPS = "excluded_apps"
        private const val KEY_TLS_SPLIT_MODE = "tls_split_mode"
        private const val KEY_DYNAMIC_ISLAND_ENABLED = "dynamic_island_enabled"
        private const val KEY_DYNAMIC_ISLAND_OVERLAY_ENABLED = "dynamic_island_overlay_enabled"
        private const val KEY_DYNAMIC_ISLAND_SCALE_PERCENT = "dynamic_island_scale_percent"
        private const val KEY_DYNAMIC_ISLAND_SIZE = "dynamic_island_size"
        private const val KEY_DYNAMIC_ISLAND_CORNER_RADIUS = "dynamic_island_corner_radius"
        private const val KEY_DYNAMIC_ISLAND_POSITION = "dynamic_island_position"
        private const val KEY_DYNAMIC_ISLAND_THEME = "dynamic_island_theme"
        private const val KEY_DYNAMIC_ISLAND_SHOW_SPEED = "dynamic_island_show_speed"
        private const val KEY_DYNAMIC_ISLAND_SHOW_PING = "dynamic_island_show_ping"
        private const val KEY_DYNAMIC_ISLAND_SHOW_FLAG = "dynamic_island_show_flag"
        private const val KEY_DYNAMIC_ISLAND_SHOW_TIMER = "dynamic_island_show_timer"
        private const val KEY_DYNAMIC_ISLAND_ANIMATION = "dynamic_island_animation"
        private const val KEY_DYNAMIC_ISLAND_PINNED_APPS = "dynamic_island_pinned_apps"
        private const val KEY_DYNAMIC_ISLAND_LED_BORDER = "dynamic_island_led_border"
        private const val KEY_DYNAMIC_ISLAND_LED_STYLE = "dynamic_island_led_style"
        private const val KEY_DYNAMIC_ISLAND_LED_SPEED = "dynamic_island_led_speed"
        private const val KEY_DYNAMIC_ISLAND_SHOW_TOWER_WAVE = "dynamic_island_show_tower_wave"
        private const val KEY_DYNAMIC_ISLAND_SHOW_SERVER_NAME = "dynamic_island_show_server_name"
        private const val KEY_DYNAMIC_ISLAND_SHOW_SESSION_DATA = "dynamic_island_show_session_data"
        private const val KEY_DYNAMIC_ISLAND_SHOW_IP_BADGE = "dynamic_island_show_ip_badge"
        private const val KEY_DYNAMIC_ISLAND_DRAGGABLE = "dynamic_island_draggable"
        private const val KEY_DYNAMIC_ISLAND_OFFSET_X = "dynamic_island_offset_x"
        private const val KEY_DYNAMIC_ISLAND_OFFSET_Y = "dynamic_island_offset_y"
        private const val KEY_SCHEDULE_ENABLED = "schedule_enabled"
        private const val KEY_SCHEDULE_START_HOUR = "schedule_start_hour"
        private const val KEY_SCHEDULE_START_MINUTE = "schedule_start_minute"
        private const val KEY_SCHEDULE_END_HOUR = "schedule_end_hour"
        private const val KEY_SCHEDULE_END_MINUTE = "schedule_end_minute"
        private const val KEY_IDLE_DISCONNECT_ENABLED = "idle_disconnect_enabled"
        private const val KEY_IDLE_TIMEOUT_MINUTES = "idle_timeout_minutes"
        private const val KEY_SESSION_DATA_LIMIT_ENABLED = "session_data_limit_enabled"
        private const val KEY_SESSION_DATA_LIMIT_MB = "session_data_limit_mb"
        private const val KEY_SMART_PING_AUTO_SWITCH = "smart_ping_auto_switch"
        private const val KEY_AUTO_CONNECT_ON_LAUNCH = "auto_connect_on_launch"
        private const val KEY_STEALTH_NOTIFICATION_ENABLED = "stealth_notification_enabled"
        private const val KEY_KEEP_ALIVE_ENABLED = "keep_alive_enabled"
        private const val KEY_KEEP_ALIVE_INTERVAL_SEC = "keep_alive_interval_sec"
        private const val KEY_AUTO_RECONNECT_ENABLED = "auto_reconnect_enabled"
        private const val KEY_APP_COLOR_THEME = "app_color_theme"
        private const val KEY_DYNAMIC_ISLAND_MOTION_STYLE = "dynamic_island_motion_style"
        private const val KEY_UNMETERED_VPN_MODE = "unmetered_vpn_mode_enabled"
        private const val KEY_TCP_BUFFER_BOOST = "tcp_buffer_boost_enabled"
        private const val KEY_BATTERY_SAVER_MODE = "battery_saver_mode_enabled"
        const val DEFAULT_SOCKS_BIND_ADDRESS = "127.0.0.1"
        const val DEFAULT_SOCKS_PORT = 1080
        const val DEFAULT_HTTP_PROXY_PORT = 8080
        const val DEFAULT_UPSTREAM_PROXY_PORT = 1080

        val DEFAULT_PINNED_ISLAND_APPS = setOf(
            "com.android.chrome",
            "org.telegram.messenger",
            "com.google.android.youtube",
        )

        val SOCKS_BIND_ADDRESS_PRESETS = listOf(
            "127.0.0.1" to "Loopback only (127.0.0.1)",
            "0.0.0.0" to "All interfaces (0.0.0.0)",
        )

        fun socksBindPresetsFor(lang: AppLanguage): List<Pair<String, String>> = when (lang) {
            AppLanguage.EN -> SOCKS_BIND_ADDRESS_PRESETS
            AppLanguage.FA -> listOf(
                "127.0.0.1" to "فقط دستگاه فعلی (۱۲۷.۰.۰.۱)",
                "0.0.0.0" to "تمام کارت‌های شبکه (۰.۰.۰.۰)",
            )
            AppLanguage.AR -> listOf(
                "127.0.0.1" to "الجهاز الحالي فقط (١٢٧.٠.٠.١)",
                "0.0.0.0" to "جميع واجهات الشبكة (٠.٠.٠.٠)",
            )
        }

        fun isValidDohUrl(value: String): Boolean {
            val trimmed = value.trim()
            if (!trimmed.startsWith("https://", ignoreCase = true)) return false
            return runCatching {
                val uri = java.net.URI.create(trimmed)
                !uri.host.isNullOrBlank()
            }.getOrDefault(false)
        }

        fun isValidIpAddress(value: String): Boolean {
            return if (android.os.Build.VERSION.SDK_INT >= 29) {
                android.net.InetAddresses.isNumericAddress(value)
            } else {
                @Suppress("DEPRECATION")
                android.util.Patterns.IP_ADDRESS.matcher(value).matches()
            }
        }

        fun isValidHostname(value: String): Boolean {
            val host = value.trim().removeSuffix(".")
            if (host.isEmpty() || host.length > 253) return false

            if (host.contains(':')) return false
            if (host.all { it.isDigit() || it == '.' }) return false
            val labels = host.split('.')
            if (labels.size < 2) return false
            return labels.all { label ->
                label.isNotEmpty() &&
                    label.length <= 63 &&
                    !label.startsWith('-') &&
                    !label.endsWith('-') &&
                    label.all { char ->
                        char == '-' || (char.code < 128 && (char.isLetterOrDigit()))
                    }
            }
        }

        fun isValidEdgeHost(value: String): Boolean {
            val host = value.trim().removeSuffix(".")
            return isValidHostname(host) || isValidIpAddress(host)
        }
    }
}
