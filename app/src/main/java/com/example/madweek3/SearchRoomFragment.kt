package com.example.madweek3

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SearchRoomFragment : Fragment() {
    private lateinit var loggedInUser: String
    private var currentRoom: Room?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "")?:""
        loggedInUser = userId?: ""

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText_toSearchRoom = view.findViewById<EditText>(R.id.editText_toSearchRoom)
        val searchButton_toSearchRoom = view.findViewById<Button>(R.id.searchButton_toSearchRoom)



        searchButton_toSearchRoom.setOnClickListener {
            val entered_roomCode = editText_toSearchRoom.text.toString()

            if (entered_roomCode.isNotEmpty()) {
                //roomCode랑 같은 방이 있는지 확인
                lifecycleScope.launch {
                    val getRoomInfo = async { getRoomInfo(entered_roomCode) }
                    getRoomInfo.await()

                    if (currentRoom != null) {

                        Log.d("searchRoom test", "${currentRoom!!.roomId} is not null")

                        if (currentRoom!!.privateLock == true) { // 해당 방이 있으면 비번 존재하는지 확인 & 체크
                            //방의 lock이 걸려있는 경우
                            val validPassword = currentRoom!!.passwordLock

                            val enterPasswordDialog = EnterPasswordDialog()
                            enterPasswordDialog.show(childFragmentManager, "EnterPasswordDialog")

                            enterPasswordDialog.setOnItemClickListener { enteredPassword ->
                                if (enteredPassword == validPassword) {
                                    Toast.makeText(requireContext(), "비밀번호 일치", Toast.LENGTH_SHORT)
                                        .show()

                                    lifecycleScope.launch {

                                        val addMember_toCurrentRoom = async {
                                            addRoomMember(
                                                loggedInUser,
                                                currentRoom!!.roomId
                                            )
                                        }
                                        addMember_toCurrentRoom.await()

                                    }
                                } else {
                                    Toast.makeText(requireContext(), "비밀번호 불일치", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        } else { //비밀번호 설정 안 되어 있을 경우
                            lifecycleScope.launch {

                                val addMember_toCurrentRoom = async {
                                    addRoomMember(
                                        loggedInUser,
                                        currentRoom!!.roomId
                                    )
                                }
                                addMember_toCurrentRoom.await()

                            }

                        }

                    }

                }



            }

                // 방에 들어간다면, 들어가기 전에 addRoomMember 호출
                // 그 fragment로 이동, 아니면 해당 방이 없다는 메세지 출력
        }
    }
    private suspend fun getRoomInfo(id: String) {
        try {
            val response = RetrofitClient.instance.getRoomInfo(id)
            if (response.isSuccessful && response.body() != null) {
                currentRoom = response.body()!!

                Log.d("searchRoom test", currentRoom!!.roomId)

            }

            else {
                Toast.makeText(requireContext(), "해당하는 room이 없습니다.", Toast.LENGTH_SHORT).show()
                Log.d("searchRoom test1", "해당 roomCode가 없습니다.")
            }
        } catch(e: Exception) {
            Log.d("Get room error", "room 을 받아오지 못했음")
        }
    }

    suspend fun addRoomMember(userId:String, current_roomId:String) {
        try {

            val response = RetrofitClient.instance.addRoomMember(userId, current_roomId)
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.UID == 200) {
                    Log.d("check","successfully add room Member")
                    Toast.makeText(requireContext(), "입장 성공", Toast.LENGTH_SHORT).show()

                    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                    val readyFragment = ReadyFragment()
                    val bundle = Bundle()
                    bundle.putString("roomId", current_roomId)
                    readyFragment.arguments = bundle

                    // Fragment 전환
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, readyFragment)
                        .commit()
                }

                else if (response.body()!!.UID ==401) {
                    Toast.makeText(requireContext(), "최대 인원을 초과하였습니다.", Toast.LENGTH_SHORT).show()
                }



            } else {
                Log.d("response failed", "실패")
            }

        } catch(e: Exception) {
            Log.e("Add RoomMember Error", "error: ${e.localizedMessage}}")
        }

    }




}