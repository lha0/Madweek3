package com.example.madweek3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ReadyFragment : Fragment() {

    private lateinit var currentRoom: Room
    private lateinit var gameStartBtn: Button
    private var userList: ArrayList<User>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ready, container, false)
        val backButton_toRoomList = view.findViewById<Button>(R.id.BackBtn)
        gameStartBtn = view.findViewById(R.id.startBtn)

        //나의 userId 얻어오기
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val my_userId = sharedPreferences.getString("userId", "")?:""

        // 현재 방의 roomId 받아오기
        val roomId = arguments?.getString("roomId")
        if (roomId != null) {
            Log.d("test", roomId)
            lifecycleScope.launch {
                val getRoomMember = async { getRoomMember(roomId) }
                getRoomMember.await()
                if (userList!=null){
                    val gridView_userList = view.findViewById<GridView>(R.id.userList)

                    val gridViewAdapter = ReadyAdapter(requireContext(), userList!!)
                    // GridView에 Adapter 설정
                    gridView_userList.adapter = gridViewAdapter
                }
                else {
                    Log.d("test", "userList is null")
                }



            }
            //뒤로 가기 버튼 누르면 UserList에서 해당 유저가 지워지도록 한다.
            backButton_toRoomList.setOnClickListener {
                deleteMyUserId(my_userId, roomId)
            }

        } else {
            Log.d("test", "roomId is null")
        }

//        val testuser1 = User(email = "1234",password="1234",nickname="sechan",level ="킹받게 멋진 세찬",score=720)
//        val testuser2 = User(email="456", password = "456", nickname="hayeong",level="새싹 세찬", score=350)


        gameStartBtn.setOnClickListener {
            val intent = Intent(requireActivity(), GameActivity::class.java)
            intent.putExtra("roomId", roomId)
            intent.putParcelableArrayListExtra("userList", userList)
            startActivity(intent)
        }


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

    fun deleteMyUserId(my_userId: String, current_roomId: String) {
        RetrofitClient.instance.deleteRoomMember(my_userId, current_roomId)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.UID ==200 ) {
                            Log.d("CHECK_READYFRAGMENT",response.body()!!.message)
                            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                            // popBackStack 호출로 최상위 Fragment를 제거
                            fragmentManager.popBackStack()

                        }
                        else {
                            Log.d("CHECK_READYFRAGMENT",response.body()!!.message)
                            Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }




                    } else {
                        Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<UserResponse>, t:Throwable) {
                    Log.e("error", t.toString())
                    Toast.makeText(context, "네트워크 오류: 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
    }

}
