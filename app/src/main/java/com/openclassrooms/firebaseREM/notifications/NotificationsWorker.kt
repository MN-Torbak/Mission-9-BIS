package com.openclassrooms.firebaseREM.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.openclassrooms.firebaseREM.R

class NotificationsWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        displayNotifications()
        return Result.success()
    }

    private fun displayNotifications(

    ) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext, "k"
        )
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle("RealEstateManager")
            .setContentText("")
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText("Your property was added successfully")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationManager = NotificationManagerCompat.from(
            applicationContext
        )
        notificationManager.notify(0, builder.build())
    }
}