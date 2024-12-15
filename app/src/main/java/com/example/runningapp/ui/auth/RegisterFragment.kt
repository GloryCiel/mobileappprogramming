package com.example.runningapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.runningapp.R
import com.example.runningapp.data.PreferredRegion
import com.example.runningapp.data.storage.UserStorage
import com.example.runningapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRegionSpinner()
        setupRegisterButton()
        setupBackToLoginButton()
    }

    private fun setupRegionSpinner() {
        val regions = PreferredRegion.values()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            regions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRegion.adapter = adapter
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            val name = binding.editName.text.toString()
            val password = binding.editPassword.text.toString()
            val selectedRegion = binding.spinnerRegion.selectedItem as PreferredRegion

            if (name.isBlank() || password.isBlank()) {
                Toast.makeText(context, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 새 유저 생성
            val newUser = UserStorage.addUser(
                context = requireContext(),
                name = name,
                password = password,
                region = selectedRegion
            )

            if (newUser != null) {
                Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBackToLoginButton() {
        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}