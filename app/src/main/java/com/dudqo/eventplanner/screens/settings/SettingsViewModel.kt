package com.dudqo.eventplanner.screens.settings

import androidx.lifecycle.ViewModel
import com.dudqo.eventplanner.screens.sign_in.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsViewModel: ViewModel() {


    private val userData = Firebase.auth.currentUser?.run {
        UserData(
            userId = uid,
            userEmail = email.toString(),
            userName = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    val userEmail = userData?.userEmail
    val userName = userData?.userName
    val profilePic = userData?.profilePictureUrl
}