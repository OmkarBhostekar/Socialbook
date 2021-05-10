package com.example.instaclone.ui.auth.fragments

import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentExploreBinding
import com.example.instaclone.databinding.FragmentLogInBinding
import com.example.instaclone.databinding.FragmentRegisterBinding
import com.example.instaclone.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _bindng: FragmentRegisterBinding? = null
    private val binding :FragmentRegisterBinding
    get() = _bindng!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentRegisterBinding.bind(view)

        binding.apply {
            btnSignup.setOnClickListener {
                if (etName.text!!.isNotEmpty() && etUserName.text!!.isNotEmpty() &&
                    etSignupEmail.text!!.isNotEmpty() && etSignupPassword.text!!.isNotEmpty()){
                    viewModel.register(
                        etName.text.toString(),
                        etUserName.text.toString() ,
                        etSignupEmail.text.toString() ,
                        etSignupPassword.text.toString()
                    )
                }
            }
            btnLogin.setOnClickListener {
                activity?.onBackPressed()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginUiState.collect{
                when(it){
                    is AuthViewModel.LoginUiState.Success -> {
//                        binding.btnSignUp.doneLoadingAnimation(
//                            ContextCompat.getColor(requireContext(),R.color.successGreen),
//                            (ResourcesCompat.getDrawable(resources, R.drawable.ic_check, null) as VectorDrawable).toBitmap()
//                        )
                        delay(1000L)
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    }
                    is AuthViewModel.LoginUiState.Error -> {
//                        binding.btnSignUp.revertAnimation()
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthViewModel.LoginUiState.Loading -> {
//                        binding.btnSignUp.startAnimation()
                        // show loading bar
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}