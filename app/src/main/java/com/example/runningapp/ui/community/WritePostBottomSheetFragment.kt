package com.example.runningapp.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.runningapp.MainActivity
import com.example.runningapp.data.CommunityTag
import com.example.runningapp.data.User
import com.example.runningapp.data.storage.CommunityPostStorage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.runningapp.databinding.BottomSheetWritePostBinding

class WritePostBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetWritePostBinding? = null
    private val binding get() = _binding!!
    private val currentUser: User by lazy {
        (activity as MainActivity).getCurrentUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWritePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomSheetBehavior()
        setupSpinner()
        setupSubmitButton()
    }

    private fun setupBottomSheetBehavior() {
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val minHeight = (screenHeight * 0.95).toInt()
        binding.bottomSheetContainer.minimumHeight = minHeight

        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = true
            isHideable = true
            skipCollapsed = true
        }
    }

    private fun setupSpinner() {
        // CommunityTag enum values를 스피너에 설정
        val tags = CommunityTag.values().map { it.toString() }
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            tags
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTag.adapter = adapter
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val content = binding.editContent.text.toString()
            val selectedTag = CommunityTag.valueOf(binding.spinnerTag.selectedItem.toString())

            // 입력 검증
            if (title.isBlank() || content.isBlank()) {
                Toast.makeText(requireContext(), "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Storage에 게시글 저장
            CommunityPostStorage.addPost(
                context = requireContext(),
                title = title,
                content = content,
                tag = selectedTag,
                userId = currentUser.id
            )

            // Fragment 갱신
            (parentFragment as? CommunityFragment)?.loadPosts()

            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}