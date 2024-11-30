package com.example.runningapp.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.runningapp.databinding.FragmentWritePostBottomSheetBinding

class WritePostBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentWritePostBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWritePostBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // BottomSheet의 최소 높이 설정
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val minHeight = (screenHeight * 0.95).toInt()
        binding.bottomSheetContainer.minimumHeight = minHeight

        // BottomSheet의 동작 설정
        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = true
            isHideable = true
            skipCollapsed = true
        }

        // 스피너 설정
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            CommunityTags.tags
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTag.adapter = adapter
        }

        // 작성완료 버튼
        binding.btnSubmit.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val tag = binding.spinnerTag.selectedItem.toString()
            val content = binding.editContent.text.toString()

            // TODO: 작성된 게시글 처리 로직 구현

            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}