package com.fort0.githubuserapp

import GhUserModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fort0.githubuserapp.databinding.GhUserRowBinding

class GhAdapter(private val context: Context, private val ghlist: ArrayList<GhUserModel>) :
    RecyclerView.Adapter<GhAdapter.GhViewHolder>() {
    lateinit var users: ArrayList<GhUserModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GhAdapter.GhViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.gh_user_row, parent, false)
        return GhViewHolder(view)
    }

    override fun onBindViewHolder(holder: GhAdapter.GhViewHolder, position: Int) {
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
            binding.ghFname.text = user.fname

            with(itemView) {
                binding.gradientView.setOnClickListener {
                    val moveActivity = Intent(itemView.context, DetailActivity::class.java)
                    moveActivity.putExtra(DetailActivity.EXTRA_DETAILS, user)
                    itemView.context.startActivity(moveActivity)
                }
            }
        }
    }
}