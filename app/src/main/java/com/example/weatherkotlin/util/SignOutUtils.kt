package com.example.weatherkotlin.util

import android.content.Context
import androidx.navigation.NavController
import com.example.weatherkotlin.R
import com.example.weatherkotlin.fragments.FragmentListToDoDirections
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun launchLogoutAlertDialog(ctx: Context, navController: NavController) {
    val alertDialogBuilder = MaterialAlertDialogBuilder(ctx)
        .setIcon(R.drawable.ic_warning)
        .setTitle("Thông báo")
        .setMessage("Bạn có chắc chắc muốn đăng xuất?")
        .setPositiveButton("Có") { _, _ ->
            AuthUI.getInstance().signOut(ctx)
            val action = FragmentListToDoDirections.actionFragmentListToDoToFragmentToDoStart(
                logoutSnackbar = 1
            )
            navController.navigate(action)
        }
        .setNegativeButton("Không") { _, _ -> }
        .create()
    alertDialogBuilder.show()
}