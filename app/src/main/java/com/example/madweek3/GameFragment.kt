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
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.security.Key
import java.util.Timer
import kotlin.concurrent.schedule

data class Keyword (
    val _id: String,
    val keyword: String
)



class GameFragment : Fragment() {
    //view
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userAdapter: GameUserAdapter
    private var userKeywords: List<UserKeyword> = listOf()
    private lateinit var leaveBtn: Button

    private lateinit var currentRoom: Room
    private lateinit var roomLeaderId: String

    private lateinit var roomId: String
    private lateinit var loggedInUserId: String
    private lateinit var userList: ArrayList<User>

    private lateinit var socketViewModel: SocketViewModel
    private lateinit var chatting_text: EditText
    private lateinit var chat_sendBtn: ImageButton
    private lateinit var chat_recycler: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private var messages: MutableList<ChatMessage> = mutableListOf()

    private lateinit var writedMessage: String

    //정답 키워드
    var ids_ofUserList: List<String> = listOf()
    private var my_keyword = ""
    private lateinit var other_keywords: MutableList<Keyword>
    private var active_users: MutableList<String> = mutableListOf()
    private var finish_users: MutableList<String> = mutableListOf()
    private lateinit var answerBtn: Button
    private lateinit var userAnswerInput: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            roomId = it.getString("roomId")?:""
            userList = it.getParcelableArrayList("userList")?:ArrayList()

            userList.sortBy { user -> user._id }
        }

        // SocketViewModel 초기화
        socketViewModel = ViewModelProvider(requireActivity()).get(SocketViewModel::class.java)

        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",
            Context.MODE_PRIVATE
        )
        loggedInUserId = sharedPreferences.getString("userId", "")?:""
        println("my id " + loggedInUserId)

        lifecycleScope.launch {
            val getRoomInfo = async { getRoomInfo(roomId) }
            getRoomInfo.await()

            roomLeaderId = currentRoom.roomLeader

            println("roomeLeaderID" + roomLeaderId)

            if (loggedInUserId == roomLeaderId) {
                ids_ofUserList = userList.map { it._id }
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
        answerBtn = view.findViewById(R.id.answerBtn)
        leaveBtn = view.findViewById(R.id.leaveBtn)
        chatting_text = view.findViewById(R.id.chat_editText)
        chat_sendBtn = view.findViewById(R.id.chat_sendBtn)
        chat_recycler = view.findViewById(R.id.chatroom)
        chatAdapter = ChatAdapter(messages)

        userRecyclerView = view.findViewById(R.id.user_view)
        userAdapter = GameUserAdapter(userKeywords)

        userRecyclerView.adapter = userAdapter
        userRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

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
            println("assign keywords success")
            val data = args[0] as JSONObject // 이것은 서버에서 받은 JSONObject입니다.
            val usersJsonArray = JSONArray(data.getString("users"))
            println("userJsonArray " + usersJsonArray)
            for (i in 0 until usersJsonArray.length()) {
                active_users.add(usersJsonArray.getString(i))
            }
            ids_ofUserList = active_users

            val user_keywords = JSONObject(data.getString("user_keywords"))
            my_keyword = user_keywords.getString(loggedInUserId)

            other_keywords = mutableListOf() // Keyword 객체를 저장할 리스트를 생성합니다.

            // JSONObject의 모든 키들에 대해 순회합니다.
            val keys = user_keywords.keys()
            while (keys.hasNext()) {
                val key = keys.next() as String
                if (key != loggedInUserId) {
                    val keywordValue = user_keywords.getString(key) // 키워드 값을 추출합니다.
                    val keywordObj = Keyword(key, keywordValue) // Keyword 객체를 생성합니다.
                    other_keywords.add(keywordObj) // 생성된 객체를 리스트에 추가합니다.
                } else {
                    continue
                }
            }
            println("my keyword is " + my_keyword)
            println("other keyword is " + other_keywords)
            setupUserRecyclerView()

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

        answerBtn.setOnClickListener {
            val dialog = AnswerDialog(requireContext())
            val wrongDialog = WaitDialog(requireContext())
            dialog.setOnAnswerClickedListener { content ->
                userAnswerInput = content
                if (content == my_keyword) {
                    socketViewModel.answerRight(roomId, loggedInUserId)
                    dialog.dismissDialog()
                } else {
                    wrongDialog.start()
                    dialog.dismissDialog()
                }
            }
            dialog.start()
        }

        leaveBtn.setOnClickListener {
            (activity as? OnGameFinishedListener)?.onGameFinished()
        }

        socketViewModel.socket.on("user right") {args ->
            println("get right event from server")
            val rightUserId = args[0] as String
            finish_users.add(rightUserId)
            active_users.remove(rightUserId)

            println("after user right " + finish_users)
            println("after user right active " + active_users)

            if (active_users.size == 1) {
                socketViewModel.finishGame(roomId)
            }
        }

        socketViewModel.socket.on("finish game") { args ->
            // "finish game" 신호를 받으면 게임 종료 처리를 수행합니다.
            activity?.runOnUiThread {
                finishFragment(finish_users)
            }
        }
        return view
    }

    private fun setupUserRecyclerView() {
        activity?.runOnUiThread {
            println("recycler view called ")
            userKeywords = combineUserKeywords()
            println("userKeywords " + userKeywords)
            userAdapter = GameUserAdapter(userKeywords)
            userRecyclerView.adapter = userAdapter
            userAdapter.notifyDataSetChanged()
        }
    }

    private fun combineUserKeywords(): List<UserKeyword> {
        return userList.mapNotNull { user ->
            other_keywords.find {
                println("it_id" + it._id)
                println("user id " + user._id)
                it._id == user._id
            }?.let { keyword ->
                UserKeyword(user.nickname, keyword.keyword)
            }
        }
    }

    fun addMessage(newMessage: ChatMessage) {
        messages.add(newMessage)
        chatAdapter.notifyItemInserted(messages.size - 1)
    }

    fun findName(userId: String): String? {
        return userList.find { it._id == userId }?.nickname
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

    private fun finishFragment(finish_users: MutableList<String>) {
        if (!isAdded) {
            println("activity is not here")
            return
        }

        val remainingUsers = ids_ofUserList.filterNot { finish_users.contains(it) }

        // 차집합에 해당하는 유저들을 finishUsers에 추가합니다.
        finish_users.addAll(remainingUsers)

        println("last finish _users " + finish_users)

        // Bundle에 finishUsers를 담아 EndFragment에 전달합니다.
        val bundle = Bundle().apply {
            putStringArrayList("finishUsers", ArrayList(finish_users))
        }

        // EndFragment를 시작합니다.
        val endFragment = EndFragment().apply {
            arguments = bundle
        }

        // EndFragment로 전환합니다.
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.game_container, endFragment)
        transaction?.commit()
    }
}