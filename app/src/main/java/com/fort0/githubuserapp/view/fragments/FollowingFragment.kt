package com.fort0.githubuserapp.view.fragments

import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.GhUserRowBinding
import com.fort0.githubuserapp.model.GhUserModel
import com.fort0.githubuserapp.view.DetailActivity
import com.fort0.githubuserapp.viewmodel.FollowingAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.gh_user_row.*
import org.json.JSONArray

class FollowingFragment : Fragment() {
    private lateinit var adapter: FollowingAdapter
    private lateinit var uname: String
    private lateinit var userid: String
    private lateinit var userpic: String
    private var users: ArrayList<GhUserModel> = arrayListOf()
    private lateinit var detailBinding: GhUserRowBinding

    companion object {
        const val EXTRA_DETAILS = "extra_details"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_following, container, false)
        adapter = FollowingAdapter(this, users)
        users.clear()
        val dataUser = activity!!.intent.getParcelableExtra<GhUserModel>(EXTRA_DETAILS) as GhUserModel
        getFollowingUserData(dataUser.uname)

        return view

    }

    private fun getFollowingUserData(username: String) {
        val client = AsyncHttpClient()
        val url = " https://api.github.com/users/$username/following"
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
                showFollowingList()

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
                userid = item.getString("id")
            }

            users.add(
                GhUserModel(
                    userid,
                    uname,
                    userpic
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showFollowingList() {
        adapter = FollowingAdapter(this, users)
        rvFollowing.layoutManager = LinearLayoutManager(activity)
        rvFollowing.adapter = adapter

    }
}