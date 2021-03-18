package com.fort0.githubuserapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imgPhoto: ImageView = findViewById(R.id.detail_userpic)
        val tvfname: TextView = findViewById(R.id.detail_fname)
        val tvuname: TextView = findViewById(R.id.detail_uname)

        val uimage = intent.getIntExtra(EXTRA_IMAGE, 0)
        val fname = intent.getStringExtra(EXTRA_FNAME)
        val uname = intent.getStringExtra(EXTRA_UNAME)

        imgPhoto.setImageResource(uimage)
        tvfname.text = fname
        tvuname.text = uname

        val mToolbar = findViewById<Toolbar>(R.id.detail_toolbar)
        setSupportActionBar(mToolbar)

        val actionbar = supportActionBar
        actionbar!!.title = R.string.user_name.toString()
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_FNAME = "extra_fname"
        const val EXTRA_UNAME = "extra_uname"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}