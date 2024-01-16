package com.example.madweek3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class ChatAdapter(private val messages: List<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 뷰 타입을 결정하기 위한 상수
    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.isOwnMessage) VIEW_TYPE_MY_MESSAGE else VIEW_TYPE_OTHER_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MY_MESSAGE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_my_item, parent, false)
            MyMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_your_item, parent, false)
            OtherMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        when (holder) {
            is MyMessageViewHolder -> holder.bind(message)
            is OtherMessageViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    // 뷰 홀더들...git
}
class MyMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val messageTextView: TextView = view.findViewById(R.id.chat_myText)
    private val nameTextView: TextView = view.findViewById(R.id.chat_Me_Name)
    fun bind(message: ChatMessage) {
        messageTextView.text = message.message
        nameTextView.text = message.name
    }
}

class OtherMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val messageTextView: TextView = view.findViewById(R.id.chat_yourText)
    private val nameTextView: TextView = view.findViewById(R.id.chat_You_Name)

    fun bind(message: ChatMessage) {
        messageTextView.text = message.message
        nameTextView.text = message.name
    }
}


