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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Timer
import kotlin.concurrent.schedule

class GameFragment : Fragment() {
    private lateinit var currentRoom: Room
    private lateinit var roomLeaderId: String

    private lateinit var roomId: String
    private lateinit var loggedInUserId: String
    private lateinit var userList: ArrayList<User>

    private lateinit var socketViewModel: SocketViewModel
    private lateinit var chatting_text: EditText
    private lateinit var chat_sendBtn: Button
    private lateinit var chat_recycler: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private var messages: MutableList<ChatMessage> = mutableListOf()

    private lateinit var writedMessage: String
    private var my_keyword = ""


    //타이머
    private var currentUserIndex: Int = 0
    private var nextUserIndex: Int = 0
    private var timer: Timer? = null
    private var answerDialog: AnswerDialog? = null
    private var waitDialog: WaitDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            roomId = it.getString("roomId")?:""
            userList = it.getParcelableArrayList("userList")?:ArrayList()

            userList.sortBy { user -> user._id }

            println("userList[0].id is " + userList[0]._id)
            println("userList[1].id is " + userList[1]._id)
        }

        // SocketViewModel 초기화
        socketViewModel = ViewModelProvider(requireActivity()).get(SocketViewModel::class.java)

        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",
            Context.MODE_PRIVATE
        )
        loggedInUserId = sharedPreferences.getString("userId", "")?:""

        lifecycleScope.launch {
            val getRoomInfo = async { getRoomInfo(roomId) }
            getRoomInfo.await()

            roomLeaderId = currentRoom.roomLeader

            println("loggedInUserId: "+loggedInUserId + ", roomLeaderId: " + roomLeaderId)

            if (loggedInUserId == roomLeaderId) {
                val ids_ofUserList = userList.map { it._id }
                println("ids_ofUserList: "+ ids_ofUserList)
                socketViewModel.assign_keywords(ids_ofUserList, roomId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        chatting_text = view.findViewById(R.id.chat_editText)
        chat_sendBtn = view.findViewById(R.id.chat_sendBtn)
        chat_recycler = view.findViewById(R.id.chatroom)
        chatAdapter = ChatAdapter(messages)



        chat_recycler.adapter = chatAdapter
        chat_recycler.layoutManager = LinearLayoutManager(context)

        chat_sendBtn.setOnClickListener {
            writedMessage = chatting_text.text.toString()
            val userName = findName(loggedInUserId)?:"본인"
            val messageClass = ChatMessage(writedMessage, userName, true)
            socketViewModel.sendMessage(roomId, loggedInUserId, messageClass)
            activity?.runOnUiThread {
                addMessage(messageClass)
            }
        }

        socketViewModel.socket.on("assign keywords success"){args ->
            val data = args[0] as JSONObject // keyword
            println("assigned keywords" + data)
            my_keyword = data.getString(loggedInUserId)
            Log.d("socket_io test", my_keyword)
        }

        socketViewModel.socket.on("get message") {args ->
            val data = args[0] as JSONObject
            val otherUserId = data.getString("userId").toString()
            if (otherUserId != loggedInUserId){
                val messageObject = data.getJSONObject("message")
                val message = messageObject.getString("message")
                val userName = findName(otherUserId) ?:"상대방"
                val messageClass = ChatMessage(message, userName, false)
                activity?.runOnUiThread {
                    addMessage(messageClass)
                }
            }

        }


        return view
    }

    fun addMessage(newMessage: ChatMessage) {
        messages.add(newMessage)
        chatAdapter.notifyItemInserted(messages.size - 1)
    }

    fun findName(userId: String): String? {
        return userList.find { it.nickname == userId }?.nickname
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
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