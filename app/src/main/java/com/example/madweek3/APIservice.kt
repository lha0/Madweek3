package com.example.madweek3

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIservice {
    @POST("/login")
    fun loginUser(@Body userData: User): Call<UserResponse>

    @POST("/signup")
    fun signupUser(@Body userData: User): Call<UserResponse>

    @GET("/user/{id}")
    suspend fun getUserInfo(@Path("id") id: String): Response<User>
}

data class User(
    val email: String,
    val password: String,
    val nickname: String,
    val level: String,
    val score: Int
)

data class UserResponse(val message: String, val UID: Int = -1)