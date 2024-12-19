// CrewPostAdapter.kt
package com.example.runningapp.ui.myPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.data.Course
import com.example.runningapp.data.storage.UserStorage
import com.example.runningapp.databinding.ItemMyPageBinding

class RunningViewHolder(
    private val binding: ItemMyPageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Course) {
        binding.apply {
            itemTag.text = "Course" // 임시
            itemTitle.text = post.title
            itemContent.text = post.description // 임시

            val user = UserStorage.getUserById(itemView.context, post.userId)

            root.setOnClickListener {
                val bottomSheet = MyPageBottomSheetFragment()
                bottomSheet.setItemDetails(
                    title = post.title,
                    content = post.description,   // 임시
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

class RunningAdapter : ListAdapter<Course, RunningViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunningViewHolder {
        return RunningViewHolder(
            ItemMyPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RunningViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}