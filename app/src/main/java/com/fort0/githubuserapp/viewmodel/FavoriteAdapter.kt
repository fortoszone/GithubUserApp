package com.fort0.githubuserapp.viewmodel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.GhUserRowBinding
import com.fort0.githubuserapp.model.GhUserModel
import com.fort0.githubuserapp.view.DetailActivity
import com.fort0.githubuserapp.view.FavoriteActivity

class FavoriteAdapter(
    private val context: Context, var listFavorites: ArrayList<GhUserModel>
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.gh_user_row, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(this.listFavorites[position])
    }

    override fun getItemCount(): Int {
        return this.listFavorites.size
    }

    inner class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = GhUserRowBinding.bind(view)
        fun bind(user: GhUserModel) {
            binding.ghUname.text = user.uname
            binding.tvId.text = user.userid

            Glide.with(itemView)
                .load(user.userpic)
                .into(binding.ghUserpic)

            with(itemView) {
                binding.user.setOnClickListener {
                    val moveActivity = Intent(itemView.context, DetailActivity::class.java)
                    moveActivity.putExtra(FavoriteActivity.EXTRA_DETAILS, user)
                    itemView.context.startActivity(moveActivity)
                }
            }
        }
    }
}