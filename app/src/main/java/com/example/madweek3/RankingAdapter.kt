package com.example.madweek3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

data class USER (
    val email: String,
    val password: String,
    val level: Int=0,
    val score: Int=0,
    val nickname: String,
)
class RankingAdapter(private val context: Context, private val dataList: List<USER>) :
    RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ranking_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.userNickname.text = data.nickname
        holder.userPoint.text = data.score.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.card_view_item)
        val userNickname: TextView = itemView.findViewById(R.id.nickName_text)
        val userPoint: TextView = itemView.findViewById(R.id.point_text)
    }
}