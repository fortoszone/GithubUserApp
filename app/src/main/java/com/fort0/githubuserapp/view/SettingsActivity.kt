package com.fort0.githubuserapp.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.utils.ReminderReceiver

class SettingsActivity : AppCompatActivity() {

    private lateinit var reminderReceiver: ReminderReceiver
    private lateinit var sharedPreferences: SharedPreferences

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
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener,
        PreferenceManager.OnPreferenceTreeClickListener {

        private var mContext: Context? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
            sp.registerOnSharedPreferenceChangeListener(this)

        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            mContext = context
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

        }

        override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) {
            val reminderReceiver = ReminderReceiver()
            val isReminderActivated = prefs?.getBoolean(key, false) ?: return

            if (isReminderActivated) {
                reminderReceiver.setReminder(mContext)
                Toast.makeText(mContext, "Reminder enabled", Toast.LENGTH_SHORT).show()

            } else {
                reminderReceiver.cancelReminder()
                Toast.makeText(context, "Reminder disabled", Toast.LENGTH_SHORT).show()

            }
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

        override fun onDetach() {
            super.onDetach()
            mContext = null
        }
    }
}