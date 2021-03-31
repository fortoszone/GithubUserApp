package com.fort0.githubuserapp

import GhUserModel
import android.app.SearchManager
import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fort0.githubuserapp.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: GhAdapter
    private lateinit var rvGh: RecyclerView
    private lateinit var fnames: Array<String>
    private lateinit var unames: Array<String>
    private lateinit var loc: Array<String>
    private lateinit var repos: Array<String>
    private lateinit var companies: Array<String>
    private lateinit var followers: Array<String>
    private lateinit var following: Array<String>
    private lateinit var userpics: TypedArray
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

        //initActionBar()
        getData()
        addItem()
        showRecyclerView()
    }

    private fun showRecyclerView() {
        rvGh.layoutManager = LinearLayoutManager(this)
        val ghAdapter = GhAdapter(this, users)
        rvGh.adapter = ghAdapter
    }

    private fun getData() {
        fnames = resources.getStringArray(R.array.name)
        unames = resources.getStringArray(R.array.username)
        loc = resources.getStringArray(R.array.location)
        repos = resources.getStringArray(R.array.repository)
        companies = resources.getStringArray(R.array.company)
        followers = resources.getStringArray(R.array.followers)
        following = resources.getStringArray(R.array.following)
        userpics = resources.obtainTypedArray(R.array.avatar)
    }

    private fun addItem() {
        users = ArrayList()
        for (position in fnames.indices) {
            val user = GhUserModel(
                fnames[position],
                unames[position],
                userpics.getResourceId(position, -1),
                companies[position],
                loc[position],
                followers[position],
                following[position],
                repos[position]
            )
            users.add(user)
        }
        adapter.users = users
    }

    private fun getUserData() {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/fortoszone"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
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

    private fun initSearch() {
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    /*private fun initActionBar() {
        val mToolbar = binding.mainToolbar
        setSupportActionBar(mToolbar)

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.app_name)

    }*/
}