package com.example.madweek3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



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
            Room(roomId = "EX34KW", roomName = "아무나 들어오세요~", numPeople = 3, privateLock = true, passwordLock = "1234", userList = mutableListOf("h","i","j")),
            Room(roomId = "EX56KW", roomName = "컴온~", numPeople = 2, privateLock = false, passwordLock = "", userList = mutableListOf("k","l")),
            // 추가적인 User 객체들을 필요에 따라 추가할 수 있습니다.
        )

        adapter = RoomListAdapter(requireContext(), roomList)
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


}