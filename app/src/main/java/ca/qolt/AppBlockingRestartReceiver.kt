package ca.qolt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class AppBlockingRestartReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "AppBlockingRestart"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Timber.tag(TAG).d("Received broadcast: ${intent.action}")

        // Check if blocking should be active
        if (!AppBlockingManager.isBlockingActive(context)) {
            Timber.tag(TAG).d("Blocking not active - ignoring broadcast")
            return
        }

        val blockedApps = AppBlockingManager.getBlockedApps(context)
        if (blockedApps.isEmpty()) {
            Timber.tag(TAG).w("Blocking active but no apps configured")
            return
        }

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                Timber.tag(TAG).i("Restarting app blocking service")
                startAppBlockingService(context, blockedApps)
            }
        }
    }

    private fun startAppBlockingService(context: Context, blockedApps: Set<String>) {
        val serviceIntent = Intent(context, AppBlockingService::class.java).apply {
            putStringArrayListExtra("blocked_apps", ArrayList(blockedApps))
        }

        try {
            context.startForegroundService(serviceIntent)
            Timber.tag(TAG).d("Service started successfully")
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Failed to start service")
        }
    }
}
