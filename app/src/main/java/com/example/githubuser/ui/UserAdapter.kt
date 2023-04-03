package com.example.githubuser.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubuser.R
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.databinding.ViewholderUserBinding
import com.example.githubuser.ui.UserAdapter.UserViewHolder

class UserAdapter (
    private val onItemClickedListener : OnItemClicked,
    private val onBookmarkClickListener : OnBookmarkClick
): ListAdapter<UserEntity, UserViewHolder>(DIFF_CALLBACK){

    inner class UserViewHolder(var binding: ViewholderUserBinding) : RecyclerView.ViewHolder (binding.root){
        fun bind(user: UserEntity){
            binding.tvSmallUsername.text = user.login
            binding.tvId.text = user.id.toString()

            Glide.with(itemView.context)
                .load(user.avatar_url)
                .placeholder(R.color.tangerine)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivSmallProfile)

            itemView.setOnClickListener{
                onItemClickedListener.onClicked(user)
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

        val ivBookmark = holder.binding.ivBookmark
        if(user.isBookmarked){
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context,
                R.drawable.ic_baseline_star_24
            ))
        }
        else{
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context,
                R.drawable.ic_baseline_star_outline_24
            ))
        }

        ivBookmark.setOnClickListener{
            onBookmarkClickListener.onBookmarked(user)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }

    interface OnItemClicked{
        fun onClicked(user : UserEntity)
    }

    interface OnBookmarkClick{
        fun onBookmarked(user: UserEntity)
    }
}