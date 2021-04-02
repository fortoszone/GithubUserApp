package com.fort0.githubuserapp.ui

import com.fort0.githubuserapp.model.GhUserModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.adapters.ViewPagerAdapter
import com.fort0.githubuserapp.databinding.ActivityDetailBinding
import com.fort0.githubuserapp.fragments.FollowersFragment
import com.fort0.githubuserapp.fragments.FollowingFragment
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataBind()
        initTabLayout()
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

    private fun initTabLayout() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(FollowingFragment(), getString(R.string.following))
        adapter.addFragment(FollowersFragment(), getString(R.string.followers))
        followViewPager.adapter = adapter
        followTabLayout.setupWithViewPager(followViewPager)

        followTabLayout.getTabAt(0)!!
        followTabLayout.getTabAt(1)!!
    }
}