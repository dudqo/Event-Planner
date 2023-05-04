package com.dudqo.eventplanner.screens.friends

import androidx.lifecycle.ViewModel
import com.dudqo.eventplanner.screens.sign_in.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FriendsViewModel(): ViewModel() {

    private val userData = Firebase.auth.currentUser?.run {
        UserData(
            userId = uid,
            userEmail = email!!,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    val userEmail = userData?.userEmail
    val userName = userData?.userName
    val profilePic = userData?.profilePictureUrl

}