package com.example.madweek3

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRoomFragment : Fragment() {

    private lateinit var roomName_editText: EditText
    private lateinit var confirmButton_toMakeRoom: Button
    private lateinit var passwordLock_editText: EditText
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
                val password =  passwordLock_editText.text.toString()
                val room_data = Room(roomId="",roomName=roomName, numPeople = 0, privateLock = checkbox_privateLock.isChecked, passwordLock = password, userList=emptyList<String>())
                RetrofitClient.instance.addNewRoom(room_data)
                    .enqueue(object : Callback<UserResponse> {
                        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                            if (response.isSuccessful && response.body() != null) {

                                println("response " + response.body())
                                roomName_editText.setText("")
                                passwordLock_editText.setText("")
                                checkbox_privateLock.isChecked = false
                                Toast.makeText(context, "방 만들기 성공!", Toast.LENGTH_SHORT).show()



                            } else {
                                Log.e("error", "making room failed.")
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


}