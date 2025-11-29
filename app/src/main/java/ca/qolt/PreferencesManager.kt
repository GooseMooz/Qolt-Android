package ca.qolt

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object PreferencesManager {

    private const val PREFS_NAME = "qolt_preferences"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_BLOCK_TIMER = "block_timer"
    private const val KEY_EMERGENCY_UNLOCK = "emergency_unlock"
    private const val KEY_DARK_MODE = "dark_mode"
    private const val KEY_LIVE_ACTIVITY = "live_activity"
    private const val KEY_APP_DELETION = "app_deletion"
    private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_PROFILE_NAME = "profile_name"
    private const val KEY_PROFILE_EMAIL = "profile_email"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        getPreferences(context).edit { putBoolean(KEY_IS_LOGGED_IN, isLoggedIn) }
    }

    fun clearLoginState(context: Context) {
        getPreferences(context).edit { clear() }
    }

    fun getBlockTimerEnabled(context: Context): Boolean =
        getPreferences(context).getBoolean(KEY_BLOCK_TIMER, true)

    fun setBlockTimerEnabled(context: Context, enabled: Boolean) {
        getPreferences(context).edit { putBoolean(KEY_BLOCK_TIMER, enabled) }
    }

    fun getEmergencyUnlockEnabled(context: Context): Boolean =
        getPreferences(context).getBoolean(KEY_EMERGENCY_UNLOCK, false)

    fun setEmergencyUnlockEnabled(context: Context, enabled: Boolean) {
        getPreferences(context).edit { putBoolean(KEY_EMERGENCY_UNLOCK, enabled) }
    }

    fun getDarkModeEnabled(context: Context): Boolean =
        getPreferences(context).getBoolean(KEY_DARK_MODE, false)

    fun setDarkModeEnabled(context: Context, enabled: Boolean) {
        getPreferences(context).edit { putBoolean(KEY_DARK_MODE, enabled) }
    }

    fun getLiveActivityEnabled(context: Context): Boolean =
        getPreferences(context).getBoolean(KEY_LIVE_ACTIVITY, true)

    fun setLiveActivityEnabled(context: Context, enabled: Boolean) {
        getPreferences(context).edit { putBoolean(KEY_LIVE_ACTIVITY, enabled) }
    }

    fun getAppDeletionEnabled(context: Context): Boolean =
        getPreferences(context).getBoolean(KEY_APP_DELETION, false)

    fun setAppDeletionEnabled(context: Context, enabled: Boolean) {
        getPreferences(context).edit { putBoolean(KEY_APP_DELETION, enabled) }
    }

    fun getNotificationsEnabled(context: Context): Boolean =
        getPreferences(context).getBoolean(KEY_NOTIFICATIONS_ENABLED, true)

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        getPreferences(context).edit { putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled) }
    }

    fun getLanguage(context: Context): String =
        getPreferences(context).getString(KEY_LANGUAGE, "English") ?: "English"

    fun setLanguage(context: Context, language: String) {
        getPreferences(context).edit { putString(KEY_LANGUAGE, language) }
    }

    fun getProfileName(context: Context): String =
        getPreferences(context).getString(KEY_PROFILE_NAME, "Franklin Au") ?: "Franklin Au"

    fun setProfileName(context: Context, name: String) {
        getPreferences(context).edit { putString(KEY_PROFILE_NAME, name) }
    }

    fun getProfileEmail(context: Context): String =
        getPreferences(context).getString(KEY_PROFILE_EMAIL, "faa30@sfu.ca") ?: "faa30@sfu.ca"

    fun setProfileEmail(context: Context, email: String) {
        getPreferences(context).edit { putString(KEY_PROFILE_EMAIL, email) }
    }

    fun setProfileImageUri(context: Context, uri: String) {
        getPreferences(context).edit { putString("profile_image_uri", uri) }
    }

    fun getProfileImageUri(context: Context): String? =
        getPreferences(context).getString("profile_image_uri", null)
}
