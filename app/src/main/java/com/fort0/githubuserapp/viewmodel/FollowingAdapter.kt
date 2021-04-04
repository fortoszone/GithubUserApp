package com.fort0.githubuserapp.viewmodel

import com.fort0.githubuserapp.model.GhUserModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.GhUserRowBinding
import com.fort0.githubuserapp.view.fragments.FollowingFragment

class FollowingAdapter(private val context: FollowingFragment, private val ghlist: ArrayList<GhUserModel>) :
    RecyclerView.Adapter<FollowingAdapter.GhViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GhViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.gh_user_row, parent, false)
        return GhViewHolder(view)
    }

    override fun onBindViewHolder(holder: GhViewHolder, position: Int) {
        holder.bind(this.ghlist[position])
    }

    override fun getItemCount(): Int {
        return this.ghlist.size
    }

    inner class GhViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = GhUserRowBinding.bind(view)
        fun bind(user: GhUserModel) {
            Glide.with(itemView)
                .load(user.userpic)
                .into(binding.ghUserpic)

            binding.ghUname.text = user.uname
            binding.tvId.text = user.userid

            with(itemView) {
                binding.user.setOnClickListener {
                    Toast.makeText(context, user.uname, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}