package com.example.madweek3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRoomFragment : Fragment() {

    private lateinit var roomName_editText: EditText
    private lateinit var confirmButton_toMakeRoom: Button
    private lateinit var passwordLock_editText: EditText
    private lateinit var current_roomId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_room, container, false)
        roomName_editText = view.findViewById<EditText>(R.id.roomName_editText)
        val checkbox_privateLock = view.findViewById<CheckBox>(R.id.checkBox_privateLock)
        passwordLock_editText = view.findViewById<EditText>(R.id.passwordLock_editText)
        confirmButton_toMakeRoom = view.findViewById<Button>(R.id.confirmButton_toMakeRoom)
        val backButton_toRoomList = view.findViewById<Button>(R.id.backButton_toRoomList)


        confirmButton_toMakeRoom.setOnClickListener {
            val roomName = roomName_editText.text.toString()

            if (roomName.isEmpty()) {
                Toast.makeText(requireContext(), "텍스트를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            else {
                //본인의 userid 가져오기
                val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("userId", "")?:""

                val password =  passwordLock_editText.text.toString()
                val room_data = Room(roomId="",roomName=roomName, numPeople = 1, privateLock = checkbox_privateLock.isChecked, passwordLock = password, userList=listOf(userId), roomLeader=userId)

                //서버에 요청
                lifecycleScope.launch {
                    val addRoom = async { addNewRoom(room_data) }
                    addRoom.await()
                    if (current_roomId!=null) {
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

                }


            }

        }

        backButton_toRoomList.setOnClickListener {
            val fragmentManager = parentFragmentManager
            if (fragmentManager.backStackEntryCount > 0) {
                fragmentManager.popBackStack()
            } else {
                activity?.onBackPressed()
            }
        }





        return view
    }

    suspend fun addNewRoom(room_data: Room) {
        try {
            val response = RetrofitClient.instance.addNewRoom(room_data)
            if (response.isSuccessful && response.body() != null) {
                Log.d("CHECK UID",response.body()!!.UID.toString())
                if (response.body()!!.UID == 200) {
                    current_roomId = response.body()!!.message
                    Toast.makeText(requireContext(), "방 만들기 성공", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(requireContext(), "방 만들기 실패", Toast.LENGTH_SHORT).show()
                }

            } else {
                Log.d("response failed", "실패")
            }

        } catch(e: Exception) {
            Log.e("Add RoomMember Error", "error: ${e.localizedMessage}}")
        }

    }


}