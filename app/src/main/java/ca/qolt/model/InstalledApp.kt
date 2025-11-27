package ca.qolt.model

import android.graphics.drawable.Drawable

/**
 * Represents an installed app on the device.
 *
 * @param packageName The unique package name of the app
 * @param appName The human-readable name of the app
 * @param icon The app's icon drawable
 * @param category The app category (e.g., CATEGORY_GAME, CATEGORY_SOCIAL, etc.)
 *                 Defaults to CATEGORY_UNDEFINED for apps without a category
 */
data class InstalledApp(
    val packageName: String,
    val appName: String,
    val icon: Drawable,
    val category: Int = android.content.pm.ApplicationInfo.CATEGORY_UNDEFINED
)
