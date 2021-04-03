package com.fort0.githubuserapp.ui

import com.fort0.githubuserapp.model.GhUserModel
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fort0.githubuserapp.adapters.GhAdapter
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.gh_user_row.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: GhAdapter
    private lateinit var rvGh: RecyclerView
    private lateinit var unames: String
    private lateinit var id: String
    private lateinit var userpics: String
    private var users: ArrayList<GhUserModel> = arrayListOf()
    private lateinit var binding: ActivityMainBinding

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
        val url = "https://api.github.com/search/users?q=${username}"
        client.addHeader("Authorization", "token d2f2cc3f88418522fdd4a1a5cdf264ec464a6068")
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
            val itemObj = items.getJSONObject(0)

            unames = itemObj.getString("login")
            id = itemObj.getInt("id").toString()
            userpics = itemObj.getString("avatar_url")

            users.add(
                GhUserModel(
                    unames,
                    id,
                    userpics
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
                if (query!!.isNotEmpty()) {
                    getUserData(query)
                    return true
                } else {
                    users.clear()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }
}