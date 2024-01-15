package com.example.madweek3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EndAdapter(private val itemList: List<User>) : RecyclerView.Adapter<EndAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ranking_endFragment: TextView = view.findViewById(R.id.result_EndFragment)
        val nickname_endFragment: TextView = view.findViewById(R.id.nickName_EndFragment)
        val point_endFragment: TextView = view.findViewById(R.id.point_EndFragment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.ranking_endFragment.text = (position+1).toString()
        holder.nickname_endFragment.text = currentItem.nickname
        val point = 1 + (itemList.size - (position+1)) * 3 // 가장 낮은 순위이면, 1점 그 위에는 +3점씩
        holder.point_endFragment.text = point.toString()

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
