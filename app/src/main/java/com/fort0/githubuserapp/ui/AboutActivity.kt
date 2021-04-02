package com.fort0.githubuserapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fort0.githubuserapp.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(findViewById(R.id.main_toolbar))

        initActionBar()

    }

    private fun initActionBar() {
        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.about)
        actionbar.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

    }

}