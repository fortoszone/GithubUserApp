package com.fort0.githubuserapp.adapters

import com.fort0.githubuserapp.model.GhUserModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fort0.githubuserapp.R
import com.fort0.githubuserapp.databinding.GhUserRowBinding
import com.fort0.githubuserapp.ui.DetailActivity

class GhAdapter(private val context: Context, private val ghlist: ArrayList<GhUserModel>) :
    RecyclerView.Adapter<GhAdapter.GhViewHolder>() {
    lateinit var users: ArrayList<GhUserModel>

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

            binding.ghId.text = user.id
            binding.ghUname.text = user.uname

            with(itemView) {
                binding.user.setOnClickListener {
                    val moveActivity = Intent(itemView.context, DetailActivity::class.java)
                    moveActivity.putExtra(DetailActivity.EXTRA_DETAILS, user)
                    itemView.context.startActivity(moveActivity)
                }
            }
        }
    }
}