package com.example.runningapp.ui.myPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.data.CommunityPost
import com.example.runningapp.data.storage.UserStorage
import com.example.runningapp.databinding.ItemMyPageBinding

class CommunityViewHolder(
    private val binding: ItemMyPageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: CommunityPost) {
        binding.apply {
            itemTag.text = post.tag.toString()
            itemTitle.text = post.title
            itemContent.text = post.content

            // 특정 ID의 사용자 정보만 가져오기
            val user = UserStorage.getUserById(itemView.context, post.userId)

            // 아이템 클릭 리스너
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

class CommunityPostAdapter : ListAdapter<CommunityPost, CommunityViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        return CommunityViewHolder(
            ItemMyPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<CommunityPost>() {
        override fun areItemsTheSame(oldItem: CommunityPost, newItem: CommunityPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CommunityPost, newItem: CommunityPost): Boolean {
            return oldItem == newItem
        }
    }
}