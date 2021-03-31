package com.fort0.githubuserapp

import GhUserModel
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.fort0.githubuserapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataBind()
    }

    private fun getDataBind() {
        val user = intent.getParcelableExtra<GhUserModel?>(EXTRA_DETAILS) as GhUserModel

        binding.detailFname.text = user.fname
        binding.detailCompany.text = user.company
        binding.detailLocation.text = user.location
        binding.followers.text = user.followers
        binding.following.text = user.following
        binding.repository.text = user.repository
        binding.detailUserpic.setImageResource(user.userpic)

        val toolbar = supportActionBar
        toolbar?.title = user.uname
    }

    companion object {
        const val EXTRA_DETAILS = "extra_details"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}