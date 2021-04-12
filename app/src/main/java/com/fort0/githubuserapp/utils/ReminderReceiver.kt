package com.fort0.githubuserapp.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fort0.githubuserapp.R
import java.util.*


class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val notificationId = 1
        const val channelId = "channel_01"

    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)
        showNotification(context)
    }

    private fun createNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context?.getString(R.string.reminder)
            val desc = context?.getString(R.string.notification_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = desc
            }

            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context?) {
        val mNotificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_github_logo)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_github_logo
                )
            )
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_desc))
            .setAutoCancel(true)

        val notification = mBuilder.build()
        mNotificationManager.notify(notificationId, notification)
    }

    fun setReminder(context: Context?) {
        val alarmIntent = Intent(context, ReminderReceiver::class.java)
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 1)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )

        val intent1 = Intent(context, ReminderReceiver::class.java)
        context.startService(intent1)
    }

    fun cancelReminder() {
        val calendar: Calendar = Calendar.getInstance()
        calendar.clear()

    }
}