package com.example.madweek3

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIservice {
    @POST("/login")
    fun loginUser(@Body userData: User): Call<UserResponse>

    @POST("/signup")
    fun signupUser(@Body userData: User): Call<UserResponse>
}

data class User(
    val email: String,
    val password: String,
    val nickname: String,
    val level: Int,
    val score: Int
)

data class UserResponse(val message: String, val UID: Int = -1)