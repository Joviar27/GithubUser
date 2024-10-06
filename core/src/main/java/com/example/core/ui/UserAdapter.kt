package com.example.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.core.R
import com.example.core.databinding.ViewholderUserBinding
import com.example.core.domain.model.User
import com.example.core.ui.UserAdapter.UserViewHolder

class UserAdapter(private val type: ListType) : ListAdapter<User, UserViewHolder>(DIFF_CALLBACK){

    lateinit var onItemClicked: ((User) -> Unit)
    lateinit var onBookmarkClicked: ((User) -> Unit)

    inner class UserViewHolder(private val binding: ViewholderUserBinding) : RecyclerView.ViewHolder (binding.root){
        fun bind(user: User){
            binding.tvSmallUsername.text = user.login
            binding.tvId.text = user.id.toString()

            if(type == ListType.USER){
                binding.ivBookmark.visibility = View.VISIBLE
                itemView.setOnClickListener{
                    onItemClicked.invoke(user)
                }
                binding.ivBookmark.setOnClickListener{
                    onBookmarkClicked.invoke(user)
                }
            }else{
                binding.ivBookmark.visibility = View.GONE
            }

            Glide.with(itemView.context)
                .load(user.avatar_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivSmallProfile)

            if(user.isBookmarked!=null && user.isBookmarked){
                binding.ivBookmark.setImageResource(R.drawable.ic_baseline_star_24)
            } else{
                binding.ivBookmark.setImageResource(R.drawable.ic_baseline_star_outline_24)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ViewholderUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<User> =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser == newUser
                }
            }
    }
}