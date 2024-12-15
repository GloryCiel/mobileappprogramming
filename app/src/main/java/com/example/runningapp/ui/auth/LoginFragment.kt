package com.example.runningapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.runningapp.MainActivity
import com.example.runningapp.R
import com.example.runningapp.data.storage.UserStorage
import com.example.runningapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupLoginButton()
        setupRegisterButton()
    }

    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {
            val name = binding.editName.text.toString()
            val password = binding.editPassword.text.toString()

            if (name.isBlank() || password.isBlank()) {
                Toast.makeText(context, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // UserStorage에서 로그인 검증
            val user = UserStorage.validateUser(requireContext(), name, password)
            if (user != null) {
                // MainActivity의 currentUser 설정
                (activity as? MainActivity)?.setCurrentUser(user)
                // 홈 화면으로 이동
                findNavController().navigate(R.id.action_loginFragment_to_nav_home)
            } else {
                Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}