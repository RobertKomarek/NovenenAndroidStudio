package com.robertkomarek.novenen.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.robertkomarek.novenen.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = NotificationManagerCompat.from(context)
        val noveneName = intent.getStringExtra("noveneName") ?: "Novene"
        val notification = NotificationCompat.Builder(context, "novene_channel_id")
            .setSmallIcon(R.drawable.bell)
            .setContentTitle("Erinnerung für $noveneName")
            .setContentText("Ihre Novene beginnt jetzt.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        //notificationManager.notify(1, notification)
    }
}