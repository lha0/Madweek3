package com.example.madweek3

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIservice {
    @POST("/login")
    fun loginUser(@Body userData: User): Call<UserResponse>

    @POST("/signup")
    fun signupUser(@Body userData: User): Call<UserResponse>


    @GET("/getAllRanking")
    fun getAllRanking(): Call<List<User>>

    @GET("/user/{id}")
    suspend fun getUserInfo(@Path("id") id: String): Response<User>


    @GET("/getAllRooms")
    fun getAllRooms(): Call<List<Room>>

    @POST("/addNewRoom")
    fun addNewRoom(@Body roomData: Room): Call<UserResponse>

    @POST("/mypageModify/{id}")
    fun modifyInfo(@Body userInfo: User, @Path("id") id:String): Call<UserResponse>


}

data class User(
    val email: String,
    val password: String,
    val nickname: String,
    val level: String="새싹 세찬이",
    val score: Int=0
)

data class Room (
    val roomId: String="", //파이썬에서 랜덤 문자열 생성 -> 6글자
    val roomName: String,
    val numPeople: Int,
    val privateLock: Boolean,
    val passwordLock: String,
    val userList: List<String>, //email로 이용
)
data class UserResponse(val message: String, val UID: Int = -1)
