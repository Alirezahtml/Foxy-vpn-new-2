package com.vauth.foxyvpn.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.vauth.foxyvpn.data.FoxyColorTheme
import com.vauth.foxyvpn.data.SettingsStore

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}

class ThemeController(private val settingsStore: SettingsStore) {

    var mode: ThemeMode by mutableStateOf(settingsStore.themeMode)
        private set

    var colorTheme: FoxyColorTheme by mutableStateOf(settingsStore.appColorTheme)
        private set

    fun resolveDark(systemInDarkTheme: Boolean): Boolean = when (mode) {
        ThemeMode.SYSTEM -> systemInDarkTheme
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    fun set(next: ThemeMode) {
        if (next == mode) return
        mode = next
        settingsStore.themeMode = next
    }

    fun toggle(systemInDarkTheme: Boolean) {
        set(if (resolveDark(systemInDarkTheme)) ThemeMode.LIGHT else ThemeMode.DARK)
    }

    fun applyColorTheme(next: FoxyColorTheme) {
        if (next == colorTheme) return
        colorTheme = next
        settingsStore.appColorTheme = next
    }

    fun cycleColorTheme() {
        val entries = FoxyColorTheme.entries
        val nextIdx = (entries.indexOf(colorTheme) + 1) % entries.size
        applyColorTheme(entries[nextIdx])
    }
}

@Composable
fun rememberThemeController(settingsStore: SettingsStore): ThemeController =
    remember(settingsStore) { ThemeController(settingsStore) }
