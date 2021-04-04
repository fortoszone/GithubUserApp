package com.fort0.githubuserapp.view

import android.content.Intent
import com.fort0.githubuserapp.model.GhUserModel
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fort0.githubuserapp.viewmodel.GhAdapter
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.gh_user_row.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: GhAdapter
    private lateinit var rvGh: RecyclerView
    private lateinit var uname: String
    private lateinit var userid: String
    private lateinit var userpic: String
    private lateinit var binding: ActivityMainBinding
    private var users: ArrayList<GhUserModel> = arrayListOf()


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
                val itemObj = items.getJSONObject(i)
                userid = itemObj.getInt("id").toString()
                uname = itemObj.getString("login")
                userpic = itemObj.getString("avatar_url")
            }

            users.add(
                GhUserModel(
                    userid,
                    uname,
                    userpic
                )
            )

            showRecyclerView()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        val search: MenuItem? = menu?.findItem(R.id.search)
        val sv: SearchView = search!!.actionView as SearchView
        sv.queryHint = getString(R.string.search)

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query!!.isNotEmpty()) {
                    getUserData(query)
                    iv_search.visibility = View.INVISIBLE
                    search_desc.visibility = View.INVISIBLE

                    true

                } else {
                    iv_search.visibility = View.VISIBLE
                    false
                }

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }
}