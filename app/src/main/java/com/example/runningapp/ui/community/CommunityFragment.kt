package com.example.runningapp.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningapp.data.storage.CommunityPostStorage
import com.example.runningapp.databinding.FragmentCommunityBinding


class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private val adapter = CommunityAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        setupRecyclerView()
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

    internal fun loadPosts() {
        val posts = CommunityPostStorage.loadPosts(requireContext())
        adapter.submitList(posts)
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
