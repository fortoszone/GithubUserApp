package com.fort0.githubuserapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class GhAdapter(private val ghlist: ArrayList<GhUser>) : RecyclerView.Adapter<GhAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.gh_user_row, parent, false)
        return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (fname, uname, userpic) = ghlist[position]

        Glide.with(holder.itemView.context)
            .load(userpic)
            .apply(RequestOptions().override(500, 500))
            .into(holder.imgPhoto)

        holder.tvfname.text = fname
        holder.tvuname.text = uname

    }

    override fun getItemCount(): Int {
        return this.ghlist.size

    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvfname: TextView = itemView.findViewById(R.id.gh_fname)
        var tvuname: TextView = itemView.findViewById(R.id.gh_uname)
        var imgPhoto: ImageView = itemView.findViewById(R.id.gh_userpic)

    }
}