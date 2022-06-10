package com.example.weatherkotlin.fragments

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherkotlin.R
import com.example.weatherkotlin.databinding.FragmentToDoStartBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FragmentToDoStart : Fragment() {

    private var _binding: FragmentToDoStartBinding? = null
    private val binding get() = _binding!!
    private val resultLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> processLoginResult(result) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentToDoStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val logoutSnackbar = FragmentToDoStartArgs.fromBundle(requireArguments()).logoutSnackbar

        if (logoutSnackbar == 1) {
            Snackbar.make(
                requireContext(),
                binding.root,
                "Đăng xuất thành công!",
                Snackbar.LENGTH_SHORT
            )
                .setAnchorView(R.id.bottom_nav)
                .show()
            requireArguments().clear()
        }

        binding.cardViewToDo.setOnClickListener {
            launchSignInFlow()
        }
    }

    private fun processLoginResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val action = FragmentToDoStartDirections.actionFragmentToDoStartToFragmentListToDo(
                    snackBarType = 3,
                    userDisplayName = FirebaseAuth.getInstance().currentUser?.displayName!!
                )
                findNavController().navigate(action)
            }
            else -> {
                val msg = if (response?.error == null) {
                    "Đăng nhập không thành công."
                } else {
                    "Đăng nhập không thành công, lỗi ${response.error?.errorCode}."
                }
                Snackbar.make(requireContext(), binding.root, msg, Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.bottom_nav)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.LoginTheme)
            .setLogo(R.drawable.to_do_list)
            .build()
        resultLauncher.launch(signInIntent)
    }
}