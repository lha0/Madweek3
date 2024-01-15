package com.example.madweek3

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Exception

class ReadyFragment : Fragment() {

    private lateinit var currentRoom: Room
    private lateinit var roomLeaderId: String
    private lateinit var readyBtn: Button
    private lateinit var gameStartBtn: Button
    private lateinit var readyUserView: TextView
    private lateinit var socketViewModel: SocketViewModel
    private lateinit var gridViewAdapter: ReadyAdapter
    private lateinit var userId: String
    private lateinit var roomId: String
    private var adapterCount: Int = 0
    private var isReady = false
    private var userList: ArrayList<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",
            Context.MODE_PRIVATE
        )
        userId = sharedPreferences.getString("userId", "")?:""

        // 현재 방의 roomId 받아오기
        roomId = arguments?.getString("roomId")?:""

        lifecycleScope.launch {
            val getRoomInfo = async { getRoomInfo(roomId) }
            getRoomInfo.await()

            roomLeaderId = currentRoom.roomLeader
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ready, container, false)

        val loggedInUserId = userId
        socketViewModel = ViewModelProvider(requireActivity()).get(SocketViewModel::class.java)
        socketViewModel.joinRoom(roomId, userId)
        socketViewModel.socket.on("join success") { args->
            Log.d("socket test", "join success")
            val data = args[0] as JSONObject
            val otherUserId = data.getString("userId").toString()
            if (otherUserId != loggedInUserId) {
                Log.d("socket test", "member update")
                lifecycleScope.launch {
                    val updateRoomMember = async { updateRoomMember(otherUserId) }
                    updateRoomMember.await()
                }
            }

        }

        val backButton_toRoomList = view.findViewById<Button>(R.id.BackBtn)
        gameStartBtn = view.findViewById(R.id.startBtn)

        //나의 userId 얻어오기
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val my_userId = sharedPreferences.getString("userId", "")?:""

        if (roomId != null) {
            Log.d("test", roomId)
            lifecycleScope.launch {
                val getRoomMember = async { getRoomMember(roomId) }
                getRoomMember.await()
                if (userList!=null){
                    val gridView_userList = view.findViewById<GridView>(R.id.userList)

                    gridViewAdapter = ReadyAdapter(requireContext(), userList!!)
                    adapterCount = gridViewAdapter.count
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
                socketViewModel.leaveRoom(roomId)
            }

        } else {
            Log.d("test", "roomId is null")
        }

//        val testuser1 = User(email = "1234",password="1234",nickname="sechan",level ="킹받게 멋진 세찬",score=720)
//        val testuser2 = User(email="456", password = "456", nickname="hayeong",level="새싹 세찬", score=350)

        socketViewModel.socket.on("leave success") {args ->
            activity?.runOnUiThread {
                val removePosition = userList!!.size-1
                userList!!.removeAt(removePosition)
                gridViewAdapter.notifyDataSetChanged()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readyBtn = view.findViewById(R.id.readyBtn)
        gameStartBtn = view.findViewById(R.id.startBtn)
        readyUserView = view.findViewById(R.id.numCount)

        //준비 버튼 클릭 시, 해당 유저 join to room
        readyBtn.setOnClickListener {
            if (isReady) {
                readyBtn.setText("준비하기")
                readyBtn.setBackgroundColor(Color.parseColor("#8b8b8b"))
                isReady = false
                socketViewModel.userUnready(roomId, userId, adapterCount)

                socketViewModel.socket?.on("user ready num") {args ->
                    val data = args[0] as JSONObject
                    activity?.runOnUiThread {
                        readyUserView.setText(data.getString("number").toString())
                    }

                }
            } else {
                readyBtn.setText("준비완료")
                readyBtn.setBackgroundColor(Color.parseColor("#c3c3c3"))
                isReady = true
                socketViewModel.userReady(roomId, userId, adapterCount)

                socketViewModel.socket?.on("user unready num") {args ->
                    val data = args[0] as JSONObject
                    activity?.runOnUiThread {
                        readyUserView.setText(data.getString("number").toString())
                    }
                }
            }
        }

        socketViewModel.socket?.on("all user ready") { args ->
            activity?.runOnUiThread {
                gameStartBtn.isEnabled = true
            }
        }

        socketViewModel.socket?.on("all user unready") { args ->
            activity?.runOnUiThread {
                gameStartBtn.isEnabled = false
            }
        }


        gameStartBtn.setOnClickListener {
            if (userId == roomLeaderId) {
                socketViewModel.gameStart(roomId)

            }

        }

        socketViewModel.socket.on("game start") {args ->
            val intent = Intent(requireActivity(), GameActivity::class.java)
            intent.putExtra("roomId", roomId)
            intent.putParcelableArrayListExtra("userList", userList)
            startActivity(intent)
        }
    }

    suspend fun getRoomMember(roomId: String) {
        try {
            val response = RetrofitClient.instance.getRoomMember(roomId)
            if (response.isSuccessful && response.body() != null) {
                userList = response.body()!!
                println("check" + userList.toString())

            } else {
                Log.d("response failed", "실패")
            }

        } catch(e: Exception) {
            Log.e("Get RoomMember Error", "error: ${e.localizedMessage}}")
        }
    }

    suspend fun updateRoomMember(otherUserId: String) {
        try {
            val response = RetrofitClient.instance.getUserInfo(otherUserId)
            if (response.isSuccessful && response.body() != null) {
                val userNickname = response.body()!!.nickname
                val userEmail = response.body()!!.email
                val userPW = response.body()!!.password
                val userLevel = response.body()!!.level
                val userScore = response.body()!!.score
                val otherUser = User(
                    _id = otherUserId,
                    email = userEmail,
                    password = userPW,
                    nickname = userNickname,
                    level = userLevel,
                    score = userScore
                )
                userList!!.add(otherUser)
                gridViewAdapter.notifyDataSetChanged()
            }

        } catch (e: Exception) {
            Log.e("Update RoomMember Error", "error: ${e.localizedMessage}}")
        }
    }

    fun deleteMyUserId(my_userId: String, current_roomId: String) {
        RetrofitClient.instance.deleteRoomMember(my_userId, current_roomId)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.UID ==200 ) {
                            // test 하기 전

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

    private suspend fun getRoomInfo(id: String) {
        try {
            val response = RetrofitClient.instance.getRoomInfo(id)
            if (response.isSuccessful && response.body() != null) {
                currentRoom = response.body()!!
            }
        } catch(e: Exception) {
            Log.d("Get room error", "room 을 받아오지 못했음")
        }
    }

}
