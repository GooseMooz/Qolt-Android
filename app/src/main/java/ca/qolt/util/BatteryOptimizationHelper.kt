package ca.qolt.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import timber.log.Timber

object BatteryOptimizationHelper {

    private const val TAG = "BatteryOptimization"

    /**
     * Check if the app is exempt from battery optimizations.
     */
    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            powerManager.isIgnoringBatteryOptimizations(context.packageName)
        } else {
            true
        }
    }

    /**
     * Open battery optimization settings for manual configuration.
     */
    fun openBatteryOptimizationSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            Timber.tag(TAG).d("Opened battery optimization settings")
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Failed to open battery settings")
            openAppSettings(context)
        }
    }

    /**
     * Get manufacturer-specific battery optimization instructions.
     */
    fun getManufacturerInstructions(): String? {
        val manufacturer = Build.MANUFACTURER.lowercase()
        return when {
            manufacturer.contains("xiaomi") ->
                "Settings > Apps > Manage apps > Qolt > Enable 'Autostart' and set Battery saver to 'No restrictions'"

            manufacturer.contains("huawei") || manufacturer.contains("honor") ->
                "Settings > Apps > Qolt > Battery > Enable 'App launch' and select 'Manage manually'"

            manufacturer.contains("oppo") ->
                "Settings > Battery > App freeze > Qolt > Set to 'Never freeze'"

            manufacturer.contains("samsung") ->
                "Settings > Apps > Qolt > Battery > Set to 'Unrestricted'"

            manufacturer.contains("oneplus") ->
                "Settings > Battery > Battery optimization > Qolt > Don't optimize"

            else -> null
        }
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
