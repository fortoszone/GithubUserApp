package com.fort0.githubuserapp.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.fort0.githubuserapp.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "dicoding channel"
        const val reminder = "reminder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings)

        createNotificationChannel()

        /*val sp: SharedPreferences = getSharedPreferences("spref", Context.MODE_PRIVATE)
        val editor = sp.edit()

        editor.apply {
            //putBoolean(reminder, switch.isChecked)
            sendNotification()

        }.apply()*/
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

        }

        override fun onPreferenceTreeClick(prefs: Preference?): Boolean {
            if (prefs?.key.equals("about")) {
                goToAbout()
            }

            return super.onPreferenceTreeClick(prefs)
        }

        private fun goToAbout() {
            val moveActivity = Intent(activity, AboutActivity::class.java)
            startActivity(moveActivity)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.reminder)
            val desc = getString(R.string.notification_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = desc
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_github_logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_github_logo))
            .setContentTitle(resources.getString(R.string.notification_title))
            .setContentText(resources.getString(R.string.notification_text))
            .setAutoCancel(true)
        val notification = mBuilder.build()
        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }
}