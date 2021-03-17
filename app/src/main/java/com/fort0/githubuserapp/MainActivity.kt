package com.fort0.githubuserapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var rvGh: RecyclerView
    private var list: ArrayList<GhUser> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGh = findViewById(R.id.rv_gh)
        rvGh.setHasFixedSize(true)

        list.addAll(GhData.listData)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        rvGh.layoutManager = LinearLayoutManager(this)
        val ghAdapter = GhAdapter(list)
        rvGh.adapter = ghAdapter

    }
}