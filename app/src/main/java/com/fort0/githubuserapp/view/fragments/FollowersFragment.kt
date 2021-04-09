package com.fort0.githubuserapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.model.GhUserModel
import com.fort0.githubuserapp.viewmodel.FollowersAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_followers.*
import org.json.JSONArray

class FollowersFragment : Fragment() {
    private lateinit var adapter: FollowersAdapter
    private var users: ArrayList<GhUserModel> = arrayListOf()
    private val followersList = MutableLiveData<ArrayList<GhUserModel>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_followers, container, false)
        adapter = FollowersAdapter(this, users)
        users.clear()
        val dataUser =
            activity!!.intent.getParcelableExtra<GhUserModel>(FollowingFragment.EXTRA_DETAILS) as GhUserModel
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
                val user = GhUserModel()
                user.uname = item.getString("login")
                user.userid = item.getString("id")
                user.userpic = item.getString("avatar_url")
                users.add(user)
            }

            followersList.postValue(users)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showFollowersList() {
        adapter = FollowersAdapter(this, users)
        rvFollowers.layoutManager = LinearLayoutManager(activity)
        rvFollowers.adapter = adapter

    }
}