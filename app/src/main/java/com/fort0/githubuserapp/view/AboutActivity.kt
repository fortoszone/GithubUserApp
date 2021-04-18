package com.fort0.githubuserapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = supportActionBar
        toolbar!!.title = getString(R.string.about)
    }
}