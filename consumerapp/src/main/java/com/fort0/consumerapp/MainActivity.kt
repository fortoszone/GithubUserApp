package com.fort0.consumerapp

import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fort0.consumerapp.Database.UserColumns.Companion.CONTENT_URI
import com.fort0.consumerapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var users: ArrayList<Favorites> = arrayListOf()

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pbFav.visibility = View.VISIBLE

        val toolbar = supportActionBar
        toolbar?.title = getString(R.string.title)

        val adapter = MainAdapter(this, users)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoritesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavoritesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorites>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorites = list
            }
        }
    }

    private fun loadFavoritesAsync() {
        val adapter = MainAdapter(this, users)
        GlobalScope.launch(Dispatchers.Main) {

            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favoriteData = deferredFavorite.await()

            if (favoriteData.size > 0) {
                adapter.listFavorites = favoriteData
                for (i in 0 until favoriteData.size) {
                    getUserData(favoriteData[i].uname)
                }

                Snackbar.make(
                    view_consumer,
                    "Favorite Count : ${favoriteData.size}",
                    Snackbar.LENGTH_SHORT
                )
                    .show()

            } else {
                adapter.listFavorites = ArrayList()
                Snackbar.make(view_consumer, "Favorite Count : 0", Snackbar.LENGTH_SHORT).show()
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
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseJsonData(response: String) {
        try {
            val jsonObject = JSONObject(response)

            val user = Favorites()
            user.uname = jsonObject.getString("login")
            user.userid = jsonObject.getString("id")
            user.userpic = jsonObject.getString("avatar_url")

            users.add(user)
            binding.pbFav.visibility = View.INVISIBLE
            showRecyclerView()

        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    private fun showRecyclerView() {
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        val adapter = MainAdapter(this, users)
        binding.rvFavorites.adapter = adapter

    }

    object MappingHelper {
        fun mapCursorToArrayList(favCursor: Cursor?): ArrayList<Favorites> {
            val favoriteList = ArrayList<Favorites>()

            favCursor?.apply {
                while (moveToNext()) {
                    val login =
                        getString(getColumnIndexOrThrow(Database.UserColumns.USERNAME))
                    favoriteList.add(Favorites(uname = login))
                }
            }

            return favoriteList
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val adapter = MainAdapter(this, users)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorites)
    }

    override fun onRestart() {
        users.clear()
        super.onRestart()
        loadFavoritesAsync()
    }
}