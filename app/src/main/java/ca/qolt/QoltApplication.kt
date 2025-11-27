package ca.qolt

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ca.qolt.workers.ServiceHealthCheckWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class QoltApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("Qolt Application started")

        scheduleServiceHealthCheck()
    }

    private fun scheduleServiceHealthCheck() {
        val healthCheckRequest = PeriodicWorkRequestBuilder<ServiceHealthCheckWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "app_blocking_health_check",
            ExistingPeriodicWorkPolicy.KEEP,
            healthCheckRequest
        )

        Timber.d("Service health check worker scheduled")
    }
}
