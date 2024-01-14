package com.example.madweek3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class ReadyFragment : Fragment() {
    private lateinit var userList : List<User>
    private lateinit var currentRoom: Room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ready, container, false)

        // 현재 방의 roomId 받아오기
        val roomId = arguments?.getString("roomId")
        if (roomId != null) {
            Log.d("test", roomId)
            lifecycleScope.launch {
                val getUserInfoDeferred = async { getRoomMember(roomId) }
                getUserInfoDeferred.await()

                val gridView_userList = view.findViewById<GridView>(R.id.userList)

                val gridViewAdapter = ReadyAdapter(requireContext(), userList)
                // GridView에 Adapter 설정
                gridView_userList.adapter = gridViewAdapter


            }
        } else {
            Log.d("test", "roomId is null")
        }

//        val testuser1 = User(email = "1234",password="1234",nickname="sechan",level ="킹받게 멋진 세찬",score=720)
//        val testuser2 = User(email="456", password = "456", nickname="hayeong",level="새싹 세찬", score=350)





        return view
    }

    suspend fun getRoomMember(roomId: String) {
        try {
            val response = RetrofitClient.instance.getRoomMember(roomId)
            if (response.isSuccessful && response.body() != null) {
                userList = response.body()!!
                Log.d("check", userList.toString())

            } else {
                Log.d("response failed", "실패")
            }

        } catch(e: Exception) {
            Log.e("Get RoomMember Error", "error: ${e.localizedMessage}}")
        }
    }

}