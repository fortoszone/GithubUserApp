package com.fort0.githubuserapp.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.ActivityMainBinding
import com.fort0.githubuserapp.model.GhUserModel
import com.fort0.githubuserapp.viewmodel.GhAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.gh_user_row.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: GhAdapter
    private lateinit var rvGh: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private var users: ArrayList<GhUserModel> = arrayListOf()
    private val searchList = MutableLiveData<ArrayList<GhUserModel>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvGh = binding.rvGh
        rvGh.setHasFixedSize(true)

        adapter = GhAdapter(this, users)
        rvGh.adapter = adapter

    }

    private fun showRecyclerView() {
        rvGh.layoutManager = LinearLayoutManager(this)
        val ghAdapter = GhAdapter(this, users)
        rvGh.adapter = ghAdapter
    }

    private fun getUserData(username: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
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
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseJsonData(response: String) {
        try {
            val jsonObject = JSONObject(response)
            val items = jsonObject.getJSONArray("items")

            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                val user = GhUserModel()
                user.uname = item.getString("login")
                user.userid = item.getString("id")
                user.userpic = item.getString("avatar_url")
                users.add(user)
            }

            searchList.postValue(users)
            showRecyclerView()
            progress_bar.visibility = View.INVISIBLE


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        val search: MenuItem? = menu?.findItem(R.id.btn_search)
        val sv: SearchView = search!!.actionView as SearchView
        sv.queryHint = getString(R.string.search)

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query!!.isNotEmpty()) {
                    users.clear()
                    adapter.notifyDataSetChanged()
                    progress_bar.visibility = View.VISIBLE
                    getUserData(query)

                    iv_search.visibility = View.INVISIBLE
                    search_desc.visibility = View.INVISIBLE
                    true

                } else {
                    users.clear()
                    false
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                users.clear()
                return false
            }
        })

        val settings: MenuItem? = menu.findItem(R.id.btn_settings)
        settings?.setOnMenuItemClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            this.startActivity(intent)
            true
        }

        val favorites: MenuItem? = menu.findItem(R.id.btn_favorite)
        favorites?.setOnMenuItemClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            this.startActivity(intent)
            true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        users.clear()
    }
}