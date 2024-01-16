package com.example.madweek3

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject


class RoomListFragment : Fragment(){

    private var adapter: RoomListAdapter ?= null
    private var roomList: List<Room> ?= null
    private var enterPasswordDialog: EnterPasswordDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RoomListAdapter(requireContext(), emptyList()) // 빈 list에 대한 adapter 설정

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView_roomList)
        val makingRoomButton = view.findViewById<Button>(R.id.makingRoomButton)



        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        Log.d("getAllRooms Test", "adapter setting")

        lifecycleScope.launch {
            val getAllRooms = async { getAllRooms() }
            getAllRooms.await()
        }



        adapter?.setOnItemClickListener{ selectedItem ->

            val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("userId", "")?:""
            if (userId.isNotEmpty()) {
                Log.d("test", "userId: $userId, roomId: ${selectedItem.roomId}")
                if (selectedItem.privateLock==true) {
                    //방의 lock이 걸려있는 경우
                    val validPassword = selectedItem.passwordLock

                    val enterPasswordDialog = EnterPasswordDialog()
                    enterPasswordDialog.show(childFragmentManager, "EnterPasswordDialog")

                    enterPasswordDialog.setOnItemClickListener { enteredPassword ->
                        if (enteredPassword == validPassword) {
                            Toast.makeText(requireContext(), "비밀번호 일치", Toast.LENGTH_SHORT).show()

                            lifecycleScope.launch {

                                val current_roomId = selectedItem.roomId.toString()

                                val addMember_toCurrentRoom = async { addRoomMember(userId, current_roomId) }
                                addMember_toCurrentRoom.await()

                            }
                        }

                        else {
                            Toast.makeText(requireContext(), "비밀번호 불일치", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                else { //방의 lock이 걸려있지 않은 경우
                    lifecycleScope.launch {

                        val current_roomId = selectedItem.roomId.toString()

                        val addMember_toCurrentRoom = async { addRoomMember(userId, current_roomId) }
                        addMember_toCurrentRoom.await()
                    }
                }
            } else {
                Log.d("test", "userId is null")
            }

        }


        recyclerView.adapter = adapter



        makingRoomButton.setOnClickListener {
            val addRoomFragment = AddRoomFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, addRoomFragment)
                .addToBackStack(null)
                .commit()

        }

        return view
    }

    suspend fun getAllRooms() {
        try {
            val response = RetrofitClient.instance.getAllRooms()
            if (response.isSuccessful && response.body() != null) {
                roomList = response!!.body()
                adapter?.updateData(roomList!!)
                Log.d("getAllRooms Test", roomList!!.toString())
            } else {
                Log.d("response failed", "실패")
            }

        } catch(e: java.lang.Exception) {
            Log.e("Get All Rooms Error", "error: ${e.localizedMessage}}")
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
                        .addToBackStack(null)
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