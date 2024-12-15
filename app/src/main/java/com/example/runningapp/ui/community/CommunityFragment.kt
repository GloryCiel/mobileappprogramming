package com.example.runningapp.ui.community

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapp.data.CommunityTag
import com.example.runningapp.data.storage.CommunityPostStorage
import com.example.runningapp.databinding.FragmentCommunityBinding


class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private val adapter = CommunityAdapter()
    private var currentTag = CommunityTag.ALL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupTagButtons()
        loadPosts()
        setupFab()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.communityRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CommunityFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupTagButtons() {
        binding.topInfo.apply {
            tagAll.setOnClickListener { filterByTag(CommunityTag.ALL) }
            tag1.setOnClickListener { filterByTag(CommunityTag.TAG1) }
            tag2.setOnClickListener { filterByTag(CommunityTag.TAG2) }
            tag3.setOnClickListener { filterByTag(CommunityTag.TAG3) }
            tag4.setOnClickListener { filterByTag(CommunityTag.TAG4) }
            tag5.setOnClickListener { filterByTag(CommunityTag.TAG5) }
        }

        binding.topInfo.apply {
            tagAll.text = CommunityTag.ALL.korName
            tag1.text = CommunityTag.TAG1.korName
            tag2.text = CommunityTag.TAG2.korName
            tag3.text = CommunityTag.TAG3.korName
            tag4.text = CommunityTag.TAG4.korName
            tag5.text = CommunityTag.TAG5.korName
        }

        // 초기 All Style 설정
        binding.topInfo.apply {
            tagAll.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            tagAll.setTypeface(null, Typeface.BOLD)
        }
    }

    private fun filterByTag(tag: CommunityTag) {
        currentTag = tag
        loadPosts()
        updateTagButtonStyles(currentTag)
    }

    internal fun loadPosts() {
        val allPosts = CommunityPostStorage.loadPosts(requireContext())
        val filteredPosts = when (currentTag) {
            CommunityTag.ALL -> allPosts
            else -> allPosts.filter { it.tag == currentTag }
        }
        adapter.submitList(filteredPosts) {
            binding.communityRecyclerview.scrollToPosition(0)
        }
    }

    private fun updateTagButtonStyles(selectedTag: CommunityTag) {
        binding.topInfo.apply {
            val defaultColor = ContextCompat.getColor(requireContext(), android.R.color.darker_gray)
            val selectedColor = ContextCompat.getColor(requireContext(), android.R.color.black)

            val tagButtons = mapOf(
                CommunityTag.ALL to tagAll,
                CommunityTag.TAG1 to tag1,
                CommunityTag.TAG2 to tag2,
                CommunityTag.TAG3 to tag3,
                CommunityTag.TAG4 to tag4,
                CommunityTag.TAG5 to tag5
            )

            tagButtons.forEach { (tag, textView) ->
                val isSelected = (tag == selectedTag) // 선택된 태그만 적용
                textView.setTextColor(if (isSelected) selectedColor else defaultColor)
                textView.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL) // 기본 글꼴 지정
            }
        }
    }

    private fun setupFab() {
        binding.fabWrite.setOnClickListener {
            WritePostBottomSheetFragment().show(
                childFragmentManager,
                "WritePostBottomSheet"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
