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

        initActionBar()
        getDataBind()
    }

    private fun getDataBind() {
        val user = intent.getParcelableExtra<GhUserModel?>(EXTRA_DETAILS) as GhUserModel

        binding.detailFname.text = user.fname
        binding.detailUname.text = user.uname
        binding.detailCompany.text = user.company
        binding.detailLocation.text = user.location
        binding.followers.text = user.followers
        binding.following.text = user.following
        binding.repository.text = user.repository
        binding.detailUserpic.setImageResource(user.userpic)
    }

    companion object {
        const val EXTRA_DETAILS = "extra_details"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initActionBar() {
        val mToolbar = findViewById<Toolbar>(R.id.detail_toolbar)
        setSupportActionBar(mToolbar)

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.user_name)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

}