package com.example.runningapp.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentCommunityBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommunityBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommunityBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemTitle: String
    private lateinit var itemContent: String
    private var itemUserImage: Int = 0
    private lateinit var itemUserName: String
    private lateinit var itemUserRank: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 표시 (바인딩 사용)
        binding.title.text = itemTitle
        binding.content.text = itemContent
        binding.userImage.setImageResource(itemUserImage)
        binding.userName.text = itemUserName
        binding.userRank.text = itemUserRank

        // BottomSheet의 최소 높이 설정
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val minHeight = (screenHeight * 0.95).toInt()
        binding.bottomSheetContainer.minimumHeight = minHeight

        // BottomSheet의 동작 설정
        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
            halfExpandedRatio = 0.8f
            isDraggable = true
            isHideable = true
            isFitToContents = true

            setPeekHeight((resources.displayMetrics.heightPixels * 0.5).toInt())

            view.post {
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
                binding.nestedScrollView.scrollTo(0, 0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 메모리 누수 방지
    }

    fun setItemDetails(
        title: String, 
        content: String, 
        userImage: Int, 
        userName: String, 
        userRank: String
    ) {
        itemTitle = title
        itemContent = content
        itemUserImage = userImage
        itemUserName = userName
        itemUserRank = userRank
    }
}