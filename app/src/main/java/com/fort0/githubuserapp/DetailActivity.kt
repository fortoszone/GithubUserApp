package com.fort0.githubuserapp

import GhUserModel
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

        val mToolbar = findViewById<Toolbar>(R.id.detail_toolbar)
        setSupportActionBar(mToolbar)

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.user_name)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val uimage = intent.getIntExtra(EXTRA_IMAGE, 0)
        val fname = intent.getStringExtra(EXTRA_FNAME)
        val uname = intent.getStringExtra(EXTRA_UNAME)
        val company = intent.getStringExtra(EXTRA_COMPANY)
        val location = intent.getStringExtra(EXTRA_LOCATION)
        val followers = intent.getStringExtra(EXTRA_FOLLOWERS)
        val following = intent.getStringExtra(EXTRA_FOLLOWING)
        val repos = intent.getStringExtra(EXTRA_REPOS)

        binding.detailFname.text = fname
        binding.detailUname.text = uname
        binding.detailCompany.text = company
        binding.detailLocation.text = location
        binding.followers.text = followers
        binding.following.text = following
        binding.repository.text = repos
        binding.detailUserpic.setImageResource(uimage)
    }

    companion object {
        const val EXTRA_LOCATION = "extra_location"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_FNAME = "extra_fname"
        const val EXTRA_UNAME = "extra_uname"
        const val EXTRA_FOLLOWERS = "extra_followers"
        const val EXTRA_FOLLOWING = "extra_following"
        const val EXTRA_REPOS = "extra_repos"
        const val EXTRA_COMPANY = "extra_company"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}