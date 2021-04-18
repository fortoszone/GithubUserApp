package com.fort0.consumerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fort0.consumerapp.databinding.FavUserRowBinding

class MainAdapter(
    private val context: Context, var listFavorites: ArrayList<Favorites>
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.fav_user_row, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(this.listFavorites[position])
    }

    override fun getItemCount(): Int {
        return this.listFavorites.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FavUserRowBinding.bind(view)
        fun bind(user: Favorites) {
            binding.ghUname.text = user.uname
            binding.tvId.text = user.userid

            Glide.with(itemView)
                .load(user.userpic)
                .into(binding.ghUserpic)

            with(itemView) {
                binding.user.setOnClickListener {
                    Toast.makeText(context, user.uname, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}