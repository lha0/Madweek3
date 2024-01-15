package com.example.madweek3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameUserAdapter(private val users: List<UserKeyword>) : RecyclerView.Adapter<GameUserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.userName)
        val userKeyword: TextView = view.findViewById(R.id.userKeyword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.userName.text = user.nickname // 유저 이름 설정
        holder.userKeyword.text = user.keyword // 유저 키워드 설정
    }

    override fun getItemCount() = users.size
}