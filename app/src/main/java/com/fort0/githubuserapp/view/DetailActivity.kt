package com.fort0.githubuserapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.ActivityDetailBinding
import com.fort0.githubuserapp.model.GhUserModel
import com.fort0.githubuserapp.view.fragments.FollowersFragment
import com.fort0.githubuserapp.view.fragments.FollowingFragment
import com.fort0.githubuserapp.viewmodel.ViewPagerAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.gh_user_row.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<GhUserModel>(EXTRA_DETAILS) as GhUserModel
        getUserData(user.uname)

        initFollowTab()

    }

    companion object {
        const val EXTRA_DETAILS = "extra_details"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getUserData(username: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val response = String(responseBody)
                parseJsonData(response)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseJsonData(response: String) {
        try {
            val jsonObject = JSONObject(response)

            val toolbar = supportActionBar
            toolbar?.title = jsonObject.getString("login")

            binding.detailFname.text = jsonObject.getString("name")
            binding.detailCompany.text = jsonObject.getString("company")
            binding.detailLocation.text = jsonObject.getString("location")
            binding.followers.text = jsonObject.getInt("followers").toString()
            binding.following.text = jsonObject.getInt("following").toString()
            binding.repository.text = jsonObject.getInt("public_repos").toString()
            Glide.with(this)
                .load(jsonObject.getString("avatar_url"))
                .into(binding.detailUserpic)

            val username = jsonObject.getString("login")
            val bundle = Bundle()
            bundle.putString("bundle_uname", username)

            val following = FollowingFragment()
            following.arguments = bundle

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initFollowTab() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(FollowingFragment(), getString(R.string.following))
        adapter.addFragment(FollowersFragment(), getString(R.string.followers))
        followViewPager.adapter = adapter
        followTab.setupWithViewPager(followViewPager)
    }

    fun fragmentMethod() {
        Toast.makeText(this, "Method called From Fragment",
            Toast.LENGTH_LONG).show()
    }

    fun getFollowingList() {

    }
}