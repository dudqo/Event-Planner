package com.dudqo.eventplanner.screens.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userEmail: String,
    val userName: String?,
    val profilePictureUrl: String?
)
