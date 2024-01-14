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
import org.w3c.dom.Text
import java.lang.Exception

class ReadyFragment : Fragment() {
    private lateinit var userList : ArrayList<User>
    private lateinit var currentRoom: Room
    private lateinit var readyBtn: Button
    private lateinit var gameStartBtn: Button
    private lateinit var readyUserView: TextView
    private lateinit var socketViewModel: SocketViewModel
    private lateinit var userId: String
    private lateinit var roomId: String
    private var adapterCount: Int = 0
    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",
            Context.MODE_PRIVATE
        )
        userId = sharedPreferences.getString("userId", "")?:""

        // 현재 방의 roomId 받아오기
        roomId = arguments?.getString("roomId")?:""

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ready, container, false)

        socketViewModel = ViewModelProvider(requireActivity()).get(SocketViewModel::class.java)
        socketViewModel.joinRoom(roomId, userId)

        if (roomId != null) {
            Log.d("test", roomId)
            lifecycleScope.launch {
                val getUserInfoDeferred = async { getRoomMember(roomId) }
                getUserInfoDeferred.await()

                val gridView_userList = view.findViewById<GridView>(R.id.userList)

                val gridViewAdapter = ReadyAdapter(requireContext(), userList)
                adapterCount = gridViewAdapter.count
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
            if (gameStartBtn.isEnabled) {
                val intent = Intent(requireActivity(), GameActivity::class.java)
                intent.putExtra("roomId", roomId)
                intent.putParcelableArrayListExtra("userList", userList)
                startActivity(intent)
            }
        }
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