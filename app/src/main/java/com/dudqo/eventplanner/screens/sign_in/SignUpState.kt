package com.dudqo.eventplanner.screens.sign_in

data class SignUpState(
    val isLoading: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val signUpError: String? = null
)