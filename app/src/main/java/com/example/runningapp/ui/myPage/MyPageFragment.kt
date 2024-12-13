package com.example.runningapp.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.runningapp.databinding.FragmentMyPageBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/*
 [kt 파일 정리]
  - MyPageFragment.kt: MyPageFragment 클래스 정의
    - setupRecyclerView(): 리사이클러뷰 설정
    - setupTagButtons(): 태그 버튼 설정 (viewpager 이동에 따라 반응)
    - loadPosts() : 태그에 따라 게시글 변경
    - onDestroyView(): 뷰가 제거될 때 호출 (default)
  - MyPageAdpater.kt: 정보 전달 어댑터 정의
    - MyPageAdapter() : 기본 정보 전달
    - CrewPostViewHolder() : Crew 정보 전달
    - CommunityPostViewHolder() : Community 정보 전달
  - BottomSheetFragment.kt: 아이템 선택 시, 해당 정보를 바인딩하여 표시

  [layout 파일 정리]
  - fragment_my_page.xml: MyPageFragment 레이아웃 정의
  - top_info_my_page.xml: 상단 사용자 정보 + tabLayout 정의
  - bottom_sheet_my_page.xml: 하단 BottomSheetDialog 레이아웃 정의
  - item_my_page: item 공통 레이아웃 정리
 */

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    // private val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        setupViewPager()
        setupRecyclerView()
        setupTagButtons()
        // observeViewModel()

        return binding.root
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = MyPageAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Crew Post"
                1 -> "Community Post"
                else -> null
            }
        }.attach()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MyPageAdapter()
        }
    }

    private fun setupTagButtons() {
        // Implement tag button setup
    }

//    // 라이프 사이클로 데이터 관찰 후 UI에 자동 반영
//    private fun observeViewModel() {
//        viewModel.posts.observe(viewLifecycleOwner, Observer { posts ->
//            // Update UI with posts
//        })
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}