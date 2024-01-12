package com.example.madweek3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.madweek3.APIservice

class RankingAdapter(private val context: Context, private val dataList: List<User>) :
    RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ranking_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.userNickname.text = data.nickname
        holder.userPoint.text = data.score.toString()
        holder.userRank.text = (position+1).toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.Ranking_cardView_item)
        val userNickname: TextView = itemView.findViewById(R.id.nickName_text)
        val userPoint: TextView = itemView.findViewById(R.id.point_text)
        val userRank: TextView = itemView.findViewById(R.id.ranking_text)
    }
}