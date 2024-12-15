// CrewPostAdapter.kt
package com.example.runningapp.ui.myPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.data.CrewPost
import com.example.runningapp.data.storage.UserStorage
import com.example.runningapp.databinding.ItemMyPageBinding

class CrewPostViewHolder(
    private val binding: ItemMyPageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: CrewPost) {
        binding.apply {
            itemTag.text = "CREW" // 임시
            itemTitle.text = post.title
            itemContent.text = post.content

            val user = UserStorage.getUserById(itemView.context, post.userId)

            root.setOnClickListener {
                val bottomSheet = MyPageBottomSheetFragment()
                bottomSheet.setItemDetails(
                    title = post.title,
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

class CrewPostAdapter : ListAdapter<CrewPost, CrewPostViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewPostViewHolder {
        return CrewPostViewHolder(
            ItemMyPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CrewPostViewHolder, position: Int) {
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