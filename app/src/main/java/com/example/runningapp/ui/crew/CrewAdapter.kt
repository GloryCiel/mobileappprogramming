package com.example.runningapp.ui.crew

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.data.CrewPost
import com.example.runningapp.data.storage.UserStorage
import com.example.runningapp.databinding.ItemCrewBinding
import com.example.runningapp.ui.crew.CrewBottomSheetFragment

class CrewViewHolder(
    private val binding: ItemCrewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: CrewPost) {
        binding.apply {
            itemTitle.text = post.title
            itemContent.text = post.content
            itemLocation.text = post.location

            val user = UserStorage.getUserById(itemView.context, post.userId)

            // 아이템 클릭 리스너
            root.setOnClickListener {
                val bottomSheet = CrewBottomSheetFragment()
                bottomSheet.setItemDetails(
                    title = post.title,
                    location = post.location,
                    content = post.content,
                    userImage = user?.profilePhotoPath ?: "file:///android_asset/default/defaultProfile.png",
                    userName = user?.name ?: "Unknown User",
                    userRank = user?.rank?.toString() ?: "BRONZE"
                )

                bottomSheet.show(
                    (itemView.context as AppCompatActivity).supportFragmentManager,
                    bottomSheet.tag
                )
            }
        }
    }
}

class CrewAdapter : ListAdapter<CrewPost, CrewViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        return CrewViewHolder(
            ItemCrewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<CrewPost>() {
        override fun areItemsTheSame(oldItem: CrewPost, newItem: CrewPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CrewPost, newItem: CrewPost): Boolean {
            return oldItem == newItem
        }
    }
} 