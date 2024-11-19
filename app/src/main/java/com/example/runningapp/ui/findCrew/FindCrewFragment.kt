package com.example.runningapp.ui.findCrew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.runningapp.databinding.FragmentFindCrewBinding

class FindCrewFragment : Fragment() {

    private var _binding: FragmentFindCrewBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val findCrewViewModel =
            ViewModelProvider(this).get(FindCrewViewModel::class.java)
        _binding = FragmentFindCrewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFindcrew
        findCrewViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}