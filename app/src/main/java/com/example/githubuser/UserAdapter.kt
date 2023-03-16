package com.example.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubuser.databinding.ViewholderUserBinding

class UserAdapter (
    private val listUser : List<User>,
    private val onItemClicked : (String) -> Unit
    ) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    inner class UserViewHolder(
        var binding: ViewholderUserBinding)
        : RecyclerView.ViewHolder (binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ViewholderUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.tvSmallUsername.text = listUser[position].login
        holder.binding.tvId.text = listUser[position].id.toString()

        Glide.with(holder.itemView.context)
            .load(listUser[position].avatarUrl)
            .placeholder(R.color.tangerine)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.ivSmallProfile)

        holder.itemView.setOnClickListener{
            onItemClicked(listUser[position].login)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}