package com.vauth.foxyvpn.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.vauth.foxyvpn.data.model.RuntimeAuth

private const val TAG = "TokenStore"

class TokenStore(context: Context) {

    private val prefs: SharedPreferences = createSafeEncryptedPrefs(context.applicationContext, "foxyvpn_tokens")

    fun saveAuth(auth: RuntimeAuth) {
        runCatching {
            prefs.edit()
                .putString(KEY_ACCESS_TOKEN, auth.accessToken)
                .putString(KEY_REFRESH_TOKEN, auth.refreshToken)
                .putLong(KEY_EXPIRES_AT, auth.expiresAtEpochSeconds)
                .apply()
        }.onFailure {
            AppLogger.w(TAG, "could not persist session", it)
        }
    }

    fun loadAuth(): RuntimeAuth? = runCatching {
        val access = prefs.getString(KEY_ACCESS_TOKEN, null)?.takeIf { it.isNotBlank() } ?: return@runCatching null
        val refresh = prefs.getString(KEY_REFRESH_TOKEN, null)?.takeIf { it.isNotBlank() }
        val expiresAt = prefs.getLong(KEY_EXPIRES_AT, 0L)
        RuntimeAuth(access, refresh, expiresAt)
    }.onFailure {
        AppLogger.w(TAG, "could not read the stored session", it)
    }.getOrNull()

    fun hasValidAccessToken(): Boolean {
        val auth = loadAuth() ?: return false
        if (auth.expiresAtEpochSeconds <= 0L) return false
        val nowSeconds = System.currentTimeMillis() / 1000
        return auth.expiresAtEpochSeconds - nowSeconds > CLOCK_SKEW_TOLERANCE_SECONDS
    }

    fun hasStoredSession(): Boolean = loadAuth() != null

    fun hasRefreshToken(): Boolean = loadAuth()?.refreshToken != null

    var pendingSessionToken: String?
        get() = runCatching { prefs.getString(KEY_PENDING_SESSION_TOKEN, null)?.takeIf { it.isNotBlank() } }.getOrNull()
        set(value) {
            runCatching {
                if (value != null) {
                    prefs.edit().putString(KEY_PENDING_SESSION_TOKEN, value).apply()
                } else {
                    prefs.edit().remove(KEY_PENDING_SESSION_TOKEN).apply()
                }
            }
        }

    fun clear() {
        runCatching { prefs.edit().clear().apply() }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRES_AT = "expires_at"
        private const val KEY_PENDING_SESSION_TOKEN = "pending_session_token"

        private const val CLOCK_SKEW_TOLERANCE_SECONDS = 60L
    }
}

internal fun createSafeEncryptedPrefs(context: Context, fileName: String): SharedPreferences {
    val firstAttempt = runCatching {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            fileName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }
    if (firstAttempt.isSuccess) return firstAttempt.getOrThrow()

    runCatching { context.deleteSharedPreferences(fileName) }
    val secondAttempt = runCatching {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            fileName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }
    return secondAttempt.getOrElse {
        context.getSharedPreferences("${fileName}_fallback", Context.MODE_PRIVATE)
    }
}
