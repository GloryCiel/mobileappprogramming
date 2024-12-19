package com.example.runningapp.ui.myPage

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.runningapp.MainActivity
import com.example.runningapp.data.MyPageTag
import com.example.runningapp.data.User
import com.example.runningapp.data.storage.CommunityPostStorage
import com.example.runningapp.data.storage.CourseStorage
import com.example.runningapp.data.storage.CrewPostStorage
import com.example.runningapp.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val runningAdapter = RunningAdapter()
    private val crewAdapter = CrewPostAdapter()
    private val communityAdapter = CommunityPostAdapter()
    private var currentTag = MyPageTag.RUNNING
    private val currentUser: User by lazy { // 메인에 설정된 이후 로드
        (activity as MainActivity).getCurrentUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        setUpTopInfo()
        setupRecyclerView()
        setupTagButtons()
        loadPosts()

        return binding.root
    }

    private fun setUpTopInfo() {
        binding.topInfo.userName.text = currentUser.name
        binding.topInfo.userRank.text = currentUser.rank.toString()  // Enum에 korName이 없어서 toString으로 대체
        binding.topInfo.userLocation.text = currentUser.preferredRegion.korName
        Glide.with(this)    // 여기는 이미지 지정
            .load(currentUser.profilePhotoPath) // 현재 유저 프로필 이미지 경로
            .circleCrop()
            .into(binding.topInfo.userProfile)  // 반영할 이미지뷰 ID
    }

    private fun setupRecyclerView() {
        binding.myPageRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            // 기본적으로 RUNNING 태그에 대해 runningAdapter를 사용하고, 현재 태그에 따라 어댑터를 설정
            adapter = when (currentTag) {
                MyPageTag.RUNNING -> runningAdapter
                MyPageTag.CREW -> crewAdapter
                MyPageTag.COMMUNITY -> communityAdapter
            }
            setHasFixedSize(true)
        }
    }

    private fun setupTagButtons() {
        binding.tagRunning.apply {
            setOnClickListener {
                filterByTag(MyPageTag.RUNNING)
                Log.d("debug", "Current tag: ${MyPageTag.RUNNING}")
            }
            text = MyPageTag.RUNNING.korName
        }

        binding.tagCrew.apply {
            setOnClickListener {
                filterByTag(MyPageTag.CREW)
                Log.d("debug", "Current tag: ${MyPageTag.CREW}")
            }
            text = MyPageTag.CREW.korName
        }

        binding.tagCommunity.apply {
            setOnClickListener {
                filterByTag(MyPageTag.COMMUNITY)
                Log.d("debug", "Current tag: ${MyPageTag.COMMUNITY}")
            }
            text = MyPageTag.COMMUNITY.korName
        }

        // 초기 러닝 기록 Style 설정
        binding.tagRunning.apply {
            setTextColor(resources.getColor(android.R.color.black, null))
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
    }

    private fun filterByTag(tag: MyPageTag) {
        currentTag = tag
        setupRecyclerView()
        loadPosts()
        updateTagButtonStyles(currentTag)
    }

    internal fun loadPosts() {
        val runningPosts = CourseStorage.getCoursesByUserId(requireContext(), currentUser.id)
        val crewPosts = CrewPostStorage.getPostsByUserId(requireContext(), currentUser.id)
        val communityPosts = CommunityPostStorage.getPostsByUserId(requireContext(), currentUser.id)

        when (currentTag) {
            MyPageTag.RUNNING -> {
                if (runningPosts.isEmpty()) {
                    runningAdapter.submitList(emptyList()) // 빈 리스트 전달
                } else {
                    runningAdapter.submitList(runningPosts) {
                        binding.myPageRecyclerview.scrollToPosition(0)
                    }
                }
            }
            MyPageTag.CREW -> {
                if (crewPosts.isEmpty()) {
                    crewAdapter.submitList(emptyList()) // 빈 리스트 전달
                } else {
                    crewAdapter.submitList(crewPosts) {
                        binding.myPageRecyclerview.scrollToPosition(0)
                    }
                }
            }
            MyPageTag.COMMUNITY -> {
                if (communityPosts.isEmpty()) {
                    communityAdapter.submitList(emptyList()) // 빈 리스트 전달
                } else {
                    communityAdapter.submitList(communityPosts) {
                        binding.myPageRecyclerview.scrollToPosition(0)
                    }
                }
            }
        }
    }

    private fun updateTagButtonStyles(selectedTag: MyPageTag) {
        val defaultColor = resources.getColor(android.R.color.darker_gray, null)
        val selectedColor = resources.getColor(android.R.color.black, null)

        val tagButtons = mapOf(
            MyPageTag.RUNNING to binding.tagRunning,
            MyPageTag.CREW to binding.tagCrew,
            MyPageTag.COMMUNITY to binding.tagCommunity
        )

        tagButtons.forEach { (tag, textView) ->
            val isSelected = (tag == selectedTag) // 선택된 태그만 적용
            textView.setTextColor(if (isSelected) selectedColor else defaultColor)
            textView.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}