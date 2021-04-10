package com.fort0.githubuserapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = supportActionBar
        toolbar?.title = getString(R.string.favorites)
    }
}