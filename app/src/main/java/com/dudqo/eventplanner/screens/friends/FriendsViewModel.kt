package com.dudqo.eventplanner.screens.friends

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FriendsViewModel(): ViewModel() {

    private val user = Firebase.auth.currentUser
}