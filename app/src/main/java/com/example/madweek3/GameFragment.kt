package com.example.madweek3

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class GameFragment : Fragment() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            roomId = it.getString("roomId")?:""
            userList = it.getParcelableArrayList("userList")?:ArrayList()
        }

        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",
            Context.MODE_PRIVATE
        )
        loggedInUserId = sharedPreferences.getString("userId", "")?:""

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

        // SocketViewModel 초기화
        socketViewModel = ViewModelProvider(requireActivity()).get(SocketViewModel::class.java)

        // ViewModel에서 소켓 가져오기
        val socket = socketViewModel.socket

        chat_recycler.adapter = chatAdapter
        chat_recycler.layoutManager = LinearLayoutManager(context)

        chat_sendBtn.setOnClickListener {
            writedMessage = chatting_text.text.toString()
            val userName = findName(loggedInUserId)?:"본인"
            val messageClass = ChatMessage(writedMessage, userName, true)
            socketViewModel.sendMessage(roomId, loggedInUserId, messageClass)
            println("send"+messageClass)
            addMessage(messageClass)
        }

        socketViewModel.socket?.on("get message") {args ->
            val data = args[0] as JSONObject
            val otherUserId = data.getString("userId").toString()
            val message = data.getString("message").toString()
            val userName = findName(otherUserId) ?:"상대방"
            val messageClass = ChatMessage(message, userName, false)
            addMessage(messageClass)
        }

        //addMessage(ChatMessage("안녕하세요", "하영", true))
        //addMessage(ChatMessage("안녕하세요", "상대", false))

        return view
    }

    fun addMessage(newMessage: ChatMessage) {
        messages.add(newMessage)
        chatAdapter.notifyItemInserted(messages.size - 1)
    }

    fun findName(userId: String): String? {
        return userList.find { it.nickname == userId }?.nickname
    }
}