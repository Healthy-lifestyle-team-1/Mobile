package com.sf.healthylifestyle.view.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sf.healthylifestyle.R
import com.sf.healthylifestyle.data.dto.auth.request.CodeRequest
import com.sf.healthylifestyle.data.dto.auth.request.LoginRequest
import com.sf.healthylifestyle.databinding.FragmentAuthBinding
import com.sf.healthylifestyle.utils.uiextensions.hide
import com.sf.healthylifestyle.utils.uiextensions.show
import com.sf.healthylifestyle.view.register.RegState
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthFragment : Fragment()
/*, HasAndroidInjector */ {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var authFragmentViewModel: AuthViewModel

    @Inject
    lateinit var vmFactory: AuthViewModel.Factory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        authFragmentViewModel =
            ViewModelProvider(this, vmFactory)[AuthViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            authFragmentViewModel.authState.collect{
                renderData(it)
            }
        }

        initBtnListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun renderData(authState: AuthState) {
        when (authState) {
            is AuthState.Auth<*> -> {
                Snackbar.make(
                    binding.root,
                    "${authFragmentViewModel.authState.value}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            is AuthState.Confirm<*> -> {
                binding.btnSubmit.text = getString(R.string.btn_submit_enter)
                with(binding) {
                    tilName.hide()
                    etName.hide()
                    tilLogin.hide()
                    etLogin.hide()
                    tvReg.hide()
                    tvRegLabel.hide()
                    tvConfirm.show()
                    tilConfirm.show()
                    etConfirm.show()
                    tvRepeat.show()
                }
                Snackbar.make(
                    binding.root,
                    "${authFragmentViewModel.authState.value}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            is AuthState.Done<*> -> {
                Snackbar.make(
                    binding.root,
                    "${authFragmentViewModel.authState.value}",
                    Snackbar.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.action_authFragment_to_homeFragment)
            }
            is AuthState.Error -> TODO()
            AuthState.Loading -> TODO()
        }
    }

    private fun initBtnListeners() = with(binding) {
        tvReg.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_registerFragment)
        }

        btnSubmit.setOnClickListener {
            when (authFragmentViewModel.authState.value){
                is AuthState.Auth<*> -> {

                    authFragmentViewModel.login(login = LoginRequest(""))
                }
                is AuthState.Confirm<*> -> {
                    authFragmentViewModel.confirm(code = CodeRequest(""))
                }
                is AuthState.Done<*> -> {
                }
                is AuthState.Error -> TODO()
                AuthState.Loading -> TODO()
            }
        }
    }
}