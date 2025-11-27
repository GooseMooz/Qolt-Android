package ca.qolt.workers

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ca.qolt.AppBlockingManager
import ca.qolt.AppBlockingService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class ServiceHealthCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "ServiceHealthCheck"
    }

    override suspend fun doWork(): Result {
        // Only proceed if blocking should be active
        if (!AppBlockingManager.isBlockingActive(applicationContext)) {
            Timber.tag(TAG).d("Blocking not active - skipping health check")
            return Result.success()
        }

        val blockedApps = AppBlockingManager.getBlockedApps(applicationContext)
        if (blockedApps.isEmpty()) {
            Timber.tag(TAG).d("No blocked apps - skipping health check")
            return Result.success()
        }

        val isServiceRunning = isServiceRunning(AppBlockingService::class.java.name)

        if (!isServiceRunning) {
            Timber.tag(TAG).w("Service not running - attempting restart")

            val intent = Intent(applicationContext, AppBlockingService::class.java).apply {
                putStringArrayListExtra("blocked_apps", ArrayList(blockedApps))
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    applicationContext.startForegroundService(intent)
                } else {
                    applicationContext.startService(intent)
                }
                Timber.tag(TAG).i("Service restarted successfully")
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to restart service")
                return Result.retry()
            }
        } else {
            Timber.tag(TAG).d("Service is running - health check passed")
        }

        return Result.success()
    }

    private fun isServiceRunning(serviceName: String): Boolean {
        val manager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        @Suppress("DEPRECATION")
        return manager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == serviceName }
    }
}
