package com.dudqo.eventplanner.screens.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val userEmail: String,
    val userName: String?,
    val profilePictureUrl: String?
)
