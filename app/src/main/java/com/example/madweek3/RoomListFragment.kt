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


class RoomListFragment : Fragment() {

    private lateinit var viewModel: RoomListViewModel
    private lateinit var adapter: RoomListAdapter
    private lateinit var roomList: List<Room>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        viewModel = ViewModelProvider(this).get(RoomListViewModel::class.java)

        val roomList: List<Room> = listOf(
            Room(roomId = "EX34KW", roomName = "아무나 들어오세요~", numPeople = 3, privateLock = true, passwordLock = "1234", userList = mutableListOf("h","i","j"), roomLeader = "65a120d15dc3f4e4c06d2977"),
            Room(roomId = "EX56KW", roomName = "컴온~", numPeople = 2, privateLock = false, passwordLock = "", userList = mutableListOf("k","l"),roomLeader="65a120d15dc3f4e4c06d2977"),
            // 추가적인 User 객체들을 필요에 따라 추가할 수 있습니다.
        )
        adapter = RoomListAdapter(requireContext(), roomList)
        adapter.setOnItemClickListener{ selectedItem ->

            val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("userId", "")?:""
            if (userId.isNotEmpty()) {
                Log.d("test", "userId: $userId")
                lifecycleScope.launch {

                    val current_roomId = selectedItem.roomId.toString()

                    val addMember_toCurrentRoom = async { addRoomMember(userId, current_roomId) }
                    addMember_toCurrentRoom.await()



                }
            } else {
                Log.d("test", "userId is null")
            }

        }


        recyclerView.adapter = adapter


        viewModel.gameRooms.observe(viewLifecycleOwner, Observer { roomList ->
            adapter.updateData(roomList)
        })

        viewModel.loadGameRooms()

        makingRoomButton.setOnClickListener {
            val addRoomFragment = AddRoomFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, addRoomFragment)
                .addToBackStack(null)
                .commit()

        }

        return view
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