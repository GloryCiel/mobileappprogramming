package com.example.runningapp.ui.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.R
import com.example.runningapp.data.storage.CrewPostStorage
import com.example.runningapp.data.storage.CrewPostStorage.loadPosts
import com.example.runningapp.databinding.FragmentCrewBinding
import com.example.runningapp.databinding.ItemCrewBinding
import com.example.runningapp.ui.community.WritePostBottomSheetFragment

class CrewFragment : Fragment() {
    private var _binding: FragmentCrewBinding? = null
    private val binding get() = _binding!!
    private val adapter = CrewAdapter()
    private var currentLocation = "중구"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrewBinding.inflate(inflater, container, false)
        
        // Toolbar 숨기기
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        setupFab()
        setupRecyclerView()
        setupLocationSpinner()
        filterByLocation() // loadPosts() 대신 filterByLocation() 호출

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.findcrewRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CrewFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupLocationSpinner() {
        val locations = FindCrewTags.locations.toMutableList()
        val spinnerAdapter = ArrayAdapter(requireContext(), 
            android.R.layout.simple_spinner_dropdown_item, locations)
        
        binding.findCrewTopInfo.selectLocation.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    currentLocation = locations[position]
                    filterByLocation(currentLocation)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    currentLocation = "중구"
                    filterByLocation()
                }
            }
        }
    }

    internal fun filterByLocation(currentLocation: String = "") {
        val allPosts = CrewPostStorage.loadPosts(requireContext())
        val filteredPosts = if (currentLocation.isEmpty()) {
            allPosts
        } else {
            allPosts.filter { post -> 
                post.location?.contains(currentLocation) ?: false
            }
        }
        adapter.submitList(filteredPosts)
    }

    private fun setupFab() {
        binding.fabWrite.setOnClickListener {
            CrewWritePostBottomSheetFragment().show(
                childFragmentManager,
                "CrewWritePostBottomSheet"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}