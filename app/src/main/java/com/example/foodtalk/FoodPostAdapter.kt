package com.example.foodtalk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodtalk.databinding.FoodPostListItemBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class FoodPostAdapter : ListAdapter<Post, FoodPostAdapter.FoodPostViewHolder>(FoodPostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodPostAdapter.FoodPostViewHolder {
        return FoodPostViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FoodPostAdapter.FoodPostViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class FoodPostViewHolder private constructor(private val binding: FoodPostListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Post) {
            binding.foodPost = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FoodPostViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FoodPostListItemBinding.inflate(layoutInflater, parent, false)
                return FoodPostViewHolder(binding)
            }
        }

    }
}

class FoodPostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.imageUri == newItem.imageUri
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}