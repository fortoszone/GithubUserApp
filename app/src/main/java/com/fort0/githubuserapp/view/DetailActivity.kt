package com.fort0.githubuserapp.view

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.ActivityDetailBinding
import com.fort0.githubuserapp.db.FavoriteContract
import com.fort0.githubuserapp.helper.FavoriteHelper
import com.fort0.githubuserapp.model.GhUserModel
import com.fort0.githubuserapp.view.fragments.FollowersFragment
import com.fort0.githubuserapp.view.fragments.FollowingFragment
import com.fort0.githubuserapp.viewmodel.ViewPagerAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.gh_user_row.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var users: ArrayList<GhUserModel> = arrayListOf()
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFollowTab()

        val user = intent.getParcelableExtra<GhUserModel>(EXTRA_DETAILS) as GhUserModel
        getUserData(user.uname)

        checkIsFavorite()
        fab_favorites.setOnClickListener {
            if (!isFavorite) {
                addToFavorite()
                fab_favorites.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, R.drawable.ic_baseline_favorite_24
                    )
                )

            } else {
                removeFavorite()
                fab_favorites.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, R.drawable.ic_baseline_favorite_border_24
                    )
                )
            }
        }

    }

    companion object {
        const val EXTRA_DETAILS = "extra_details"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getUserData(username: String) {
        progress_bar.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "fortoszone")
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

            progress_bar.visibility = View.INVISIBLE

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

    override fun onBackPressed() {
        super.onBackPressed()
        users.clear()
    }


    private fun addToFavorite() {
        val user = intent.getParcelableExtra<GhUserModel>(EXTRA_DETAILS) as GhUserModel
        val favoriteHelper = FavoriteHelper(this)
        val values = ContentValues()

        favoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        values.put(FavoriteContract.FavoriteColumns.COLUMN_NAME_LOGIN, user.uname)
        favoriteHelper.insert(values)
        favoriteHelper.close()

        Toast.makeText(this, "${user.uname} - added to favorite", Toast.LENGTH_SHORT).show()
        isFavorite = true
    }

    private fun removeFavorite() {
        val user = intent.getParcelableExtra<GhUserModel>(EXTRA_DETAILS) as GhUserModel
        val favoriteHelper = FavoriteHelper(this)
        val values = ContentValues()

        favoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        favoriteHelper.deleteById(user.uname)
        favoriteHelper.insert(values)
        favoriteHelper.close()

        Toast.makeText(this, "${user.uname} - removed from favorite", Toast.LENGTH_SHORT).show()
        isFavorite = false

    }

    private fun checkIsFavorite() {
        val user = intent.getParcelableExtra<GhUserModel>(EXTRA_DETAILS) as GhUserModel
        val favoriteHelper = FavoriteHelper(this)

        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                favoriteHelper.getInstance(applicationContext)
                favoriteHelper.open()

                val cursor = favoriteHelper.queryById(user.uname)
                MappingHelper.mapCursorToObject(cursor)
            }

            val favPointer = deferredNotes.await()
            if (favPointer.uname == user.uname) {
                fab_favorites.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity, R.drawable.ic_baseline_favorite_24
                    )
                )
                isFavorite = true


            } else {
                fab_favorites.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity, R.drawable.ic_baseline_favorite_border_24
                    )
                )
                isFavorite = false
            }
            favoriteHelper.close()

        }
    }

    object MappingHelper {
        fun mapCursorToObject(favCursor: Cursor?): GhUserModel {
            var favoriteList = GhUserModel()
            favCursor?.apply {
                if (favCursor.moveToFirst()) {
                    val id =
                        favCursor.getString(favCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteColumns.COLUMN_NAME_LOGIN))
                    favoriteList = GhUserModel(uname = id)
                    favCursor.close()
                }
            }
            return favoriteList
        }
    }
}