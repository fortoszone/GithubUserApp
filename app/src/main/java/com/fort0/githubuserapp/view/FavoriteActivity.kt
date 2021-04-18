package com.fort0.githubuserapp.view

import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.ActivityFavoriteBinding
import com.fort0.githubuserapp.db.DatabaseUser.UserColumns.Companion.CONTENT_URI
import com.fort0.githubuserapp.db.FavoriteContract
import com.fort0.githubuserapp.helper.FavoriteHelper
import com.fort0.githubuserapp.model.GhUserModel
import com.fort0.githubuserapp.viewmodel.FavoriteAdapter
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.gh_user_row.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private var users: ArrayList<GhUserModel> = arrayListOf()

    companion object {
        const val EXTRA_DETAILS = "extra_details"
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = supportActionBar
        toolbar?.title = getString(R.string.favorites)
        binding.pbFav.visibility = View.VISIBLE

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        val favoriteHelper = FavoriteHelper(this)
        favoriteHelper.open()
        if (savedInstanceState == null) {
            loadFavoritesAsync()
            contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        } else {
            val list = savedInstanceState.getParcelableArrayList<GhUserModel>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorites = list
            }
        }
    }

    private fun loadFavoritesAsync() {
        val favoriteHelper = FavoriteHelper(this)
        val adapter = FavoriteAdapter(this, users)
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                favoriteHelper.open()
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favData = deferredNotes.await()

            if (favData.size > 0) {
                adapter.listFavorites = favData
                for (i in 0 until favData.size) {
                    getUserData(favData[i].uname)
                }

                Snackbar.make(favorite, "Favorite Count : ${favData.size}", Snackbar.LENGTH_SHORT)
                    .show()

            } else {
                adapter.listFavorites = ArrayList()
                Snackbar.make(favorite, "Favorite Count : 0", Snackbar.LENGTH_SHORT).show()
                binding.pbFav.visibility = View.INVISIBLE

            }
        }
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
                Toast.makeText(this@FavoriteActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseJsonData(response: String) {
        try {
            val jsonObject = JSONObject(response)

            val user = GhUserModel()
            user.uname = jsonObject.getString("login")
            user.userid = jsonObject.getString("id")
            user.userpic = jsonObject.getString("avatar_url")

            users.add(user)
            showRecyclerView()
            contentResolver.notifyChange(CONTENT_URI, null)

            binding.pbFav.visibility = View.INVISIBLE

        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    private fun showRecyclerView() {
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        val adapter = FavoriteAdapter(this, users)
        binding.rvFavorites.adapter = adapter

    }

    object MappingHelper {
        fun mapCursorToArrayList(favCursor: Cursor?): ArrayList<GhUserModel> {
            val favoriteList = ArrayList<GhUserModel>()

            favCursor?.apply {
                while (moveToNext()) {
                    val login =
                        getString(getColumnIndexOrThrow(FavoriteContract.FavoriteColumns.COLUMN_NAME_LOGIN))
                    favoriteList.add(GhUserModel(uname = login))
                }
            }

            return favoriteList
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRestart() {
        binding.pbFav.visibility = View.VISIBLE
        super.onRestart()
        users.clear()
        loadFavoritesAsync()
        binding.pbFav.visibility = View.INVISIBLE
        contentResolver.notifyChange(CONTENT_URI, null)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val adapter = FavoriteAdapter(this, users)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorites)
    }

    override fun onDestroy() {
        super.onDestroy()
        users.clear()
        val favoriteHelper = FavoriteHelper(this)
        favoriteHelper.close()
    }
}