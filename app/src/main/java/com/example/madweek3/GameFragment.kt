package com.example.madweek3

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

class GameFragment : Fragment() {

    private lateinit var socketViewModel: SocketViewModel
    private lateinit var chatting_text: EditText
    private lateinit var chat_sendBtn: Button
    private lateinit var chat_recycler: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private var messages: MutableList<ChatMessage> = mutableListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        addMessage(ChatMessage("안녕하세요", "하영", true))
        addMessage(ChatMessage("안녕하세요", "상대", false))


        // SocketViewModel 초기화
        socketViewModel = ViewModelProvider(requireActivity()).get(SocketViewModel::class.java)

        // ViewModel에서 소켓 가져오기
        val socket = socketViewModel.socket
        return view
    }

    fun addMessage(newMessage: ChatMessage) {
        messages.add(newMessage)
        chatAdapter.notifyItemInserted(messages.size - 1)
    }
}