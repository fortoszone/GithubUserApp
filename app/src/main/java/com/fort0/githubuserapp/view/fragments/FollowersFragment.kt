package com.fort0.githubuserapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.FragmentFollowingBinding
import com.fort0.githubuserapp.model.GhUserModel
import com.fort0.githubuserapp.viewmodel.FollowersAdapter
import com.fort0.githubuserapp.viewmodel.FollowingAdapter
import com.fort0.githubuserapp.viewmodel.ViewPagerAdapter
import com.fort0.githubuserapp.viewmodel.GhAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_followers.*
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray

class FollowersFragment : Fragment() {
    private lateinit var adapter: FollowersAdapter
    private lateinit var uname: String
    private lateinit var userid: String
    private lateinit var userpic: String
    private var users: ArrayList<GhUserModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_followers, container, false)
        adapter = FollowersAdapter(this, users)
        users.clear()
        val dataUser = activity!!.intent.getParcelableExtra<GhUserModel>(FollowingFragment.EXTRA_DETAILS) as GhUserModel
        getFollowerUser(dataUser.uname)

        return view
    }

    private fun getFollowerUser(username: String) {
        val client = AsyncHttpClient()
        val url = " https://api.github.com/users/$username/followers"
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
                showFollowersList()
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
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseJsonData(response: String) {
        try {
            val items = JSONArray(response)

            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                uname = item.getString("login")
                userpic = item.getString("avatar_url")
                userid = item.getInt("id").toString()
            }

            users.add(
                GhUserModel(
                    userid,
                    uname,
                    userpic
                )
            )

        } catch (e:  Exception) {
            e.printStackTrace()
        }
    }

    private fun showFollowersList() {
        adapter = FollowersAdapter(this, users)
        rvFollowers.layoutManager = LinearLayoutManager(activity)
        rvFollowers.adapter = adapter

    }
}