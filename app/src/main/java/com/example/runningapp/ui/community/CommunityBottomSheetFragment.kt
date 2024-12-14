package com.example.runningapp.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.runningapp.databinding.BottomSheetCommunityBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommunityBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCommunityBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemTitle: String
    private lateinit var itemContent: String
    private lateinit var itemUserImage: String
    private lateinit var itemUserName: String
    private lateinit var itemUserRank: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 바인딩
        binding.title.text = itemTitle
        binding.content.text = itemContent
        binding.userName.text = itemUserName
        binding.userRank.text = itemUserRank
        Glide.with(this)    // 여기는 이미지 지정
            .load(itemUserImage)
            .circleCrop()
            .into(binding.userImage)  // BottomSheet의 이미지뷰 ID

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

    fun setItemDetails( // item 저장만 담당
        title: String, 
        content: String, 
        userImage: String,
        userName: String, 
        userRank: String
    ) {
        itemTitle = title
        itemContent = content
        itemUserImage = userImage
        itemUserName = userName
        itemUserRank = userRank
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 메모리 누수 방지
    }
}