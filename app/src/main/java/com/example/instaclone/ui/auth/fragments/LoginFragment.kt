package com.example.instaclone.ui.auth.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentLogInBinding
import com.example.instaclone.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_log_in) {

    private var _bindng: FragmentLogInBinding? = null
    private val binding: FragmentLogInBinding
    get() = _bindng!!
    private val viewModel: AuthViewModel by viewModels()
    lateinit var store: DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentLogInBinding.bind(view)
        store = requireContext().createDataStore("Settings")

        binding.apply {
            btnLogIn.setOnClickListener {
                if (etLoginEmail.text!!.isNotEmpty() && etLoginPassword.text!!.isNotEmpty()){
                    viewModel.login(etLoginEmail.text.toString(),etLoginPassword.text.toString())
                }
            }

            tvSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginUiState.collect {
                when(it){
                    is AuthViewModel.LoginUiState.Success -> {
//                        binding.btnLogIn.doneLoadingAnimation(
//                            ContextCompat.getColor(requireContext(),R.color.successGreen),
//                            (ResourcesCompat.getDrawable(resources, R.drawable.ic_check, null) as VectorDrawable).toBitmap()
//                        )
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        Log.d("MyActivity", "onViewCreated: after findnavcontroller")
                    }
                    is AuthViewModel.LoginUiState.Error -> {
                        binding.btnLogIn.revertAnimation()
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthViewModel.LoginUiState.Loading -> {
                        binding.btnLogIn.startAnimation()
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