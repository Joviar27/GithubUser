package com.example.githubuser.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubuser.R
import com.example.githubuser.data.local.entity.FollowEntity
import com.example.githubuser.databinding.ViewholderFollowBinding

class FollowAdapter : ListAdapter<FollowEntity, FollowAdapter.FollowViewHolder>(DIFF_CALLBACK){

    inner class FollowViewHolder(private var binding: ViewholderFollowBinding) : RecyclerView.ViewHolder (binding.root){
        fun bind(follow : FollowEntity){
            binding.tvSmallUsername.text = follow.login
            binding.tvId.text = follow.id.toString()

            Glide.with(itemView.context)
                .load(follow.avatar_url)
                .placeholder(R.color.tangerine)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivSmallProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val binding = ViewholderFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val follow = getItem(position)
        holder.bind(follow)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<FollowEntity> =
            object : DiffUtil.ItemCallback<FollowEntity>() {
                override fun areItemsTheSame(oldFollow: FollowEntity, newFollow: FollowEntity): Boolean {
                    return oldFollow.id==newFollow.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldFollow: FollowEntity, newFollow: FollowEntity): Boolean {
                    return oldFollow==newFollow
                }
            }
    }
}