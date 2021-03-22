package com.fort0.githubuserapp

import GhUserModel
import android.content.res.TypedArray
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fort0.githubuserapp.databinding.ActivityMainBinding

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

        setSupportActionBar(findViewById(R.id.main_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(true)

        rvGh = binding.rvGh
        rvGh.setHasFixedSize(true)

        adapter = GhAdapter(this, users)
        rvGh.adapter = adapter

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

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(applicationContext, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/
}