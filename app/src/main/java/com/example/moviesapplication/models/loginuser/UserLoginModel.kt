package com.example.moviesapplication.models.loginuser

data class UserLoginModel(
    var access_token: String?,
    var expires_in: Int?,
    var refresh_token: String?,
    var token_type: String?
)