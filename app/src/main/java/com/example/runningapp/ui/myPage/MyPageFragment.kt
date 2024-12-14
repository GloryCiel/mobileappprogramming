package com.example.runningapp.ui.myPage

import android.os.Bundle
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
import com.example.runningapp.data.storage.CrewPostStorage
import com.example.runningapp.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val communityAdapter = CommunityPostAdapter()
    private val crewAdapter = CrewPostAdapter()
    private var currentTag = MyPageTag.CREW
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
            adapter = if (currentTag == MyPageTag.CREW) crewAdapter else communityAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupTagButtons() {
        binding.tagCrew.apply {
            setOnClickListener { filterByTag(MyPageTag.CREW) }
            text = MyPageTag.CREW.korName
        }

        binding.tagCommunity.apply {
            setOnClickListener { filterByTag(MyPageTag.COMMUNITY) }
            text = MyPageTag.COMMUNITY.korName
        }
    }

    private fun filterByTag(tag: MyPageTag) {
        currentTag = tag
        setupRecyclerView()
        loadPosts()
    }

    internal fun loadPosts() {
        val crewPosts = CrewPostStorage.getPostsByUserId(requireContext(), currentUser.id)
        val communityPosts = CommunityPostStorage.getPostsByUserId(requireContext(), currentUser.id)
        if (currentTag == MyPageTag.CREW) {
            crewAdapter.submitList(crewPosts) {
                binding.myPageRecyclerview.scrollToPosition(0)
            }
        } else {
            communityAdapter.submitList(communityPosts) {
                binding.myPageRecyclerview.scrollToPosition(0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}