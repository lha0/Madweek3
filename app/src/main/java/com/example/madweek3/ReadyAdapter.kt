package com.example.madweek3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
class ReadyAdapter(private val context: Context, private val userList: List<User>) : BaseAdapter(){

    // getCount 메서드는 Adapter에 표시할 아이템의 총 개수를 반환합니다.
    override fun getCount(): Int {
        return userList.size
    }

    // getItem 메서드는 특정 위치(position)의 아이템을 반환합니다.
    override fun getItem(position: Int): Any {
        return userList[position]
    }

    // getItemId 메서드는 특정 위치(position)의 아이템의 ID를 반환합니다.
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // getView 메서드는 각 아이템의 View를 생성하고 반환합니다.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        val holder: ViewHolder

        if (itemView == null) {
            // 새로운 View를 생성할 때
            itemView = LayoutInflater.from(context).inflate(R.layout.readylist_item, parent, false)
            holder = ViewHolder(itemView)
            itemView.tag = holder
        } else {
            // 이미 생성된 View를 재활용할 때
            holder = itemView.tag as ViewHolder
        }

        // 데이터 설정
        val data = userList[position]
        holder.ready_userId.text = data.nickname
        holder.ready_userLevel.text = data.level

        return itemView!!
    }

    // ViewHolder 패턴을 사용하여 View의 재사용을 최적화합니다.
    private class ViewHolder(view: View) {
        val ready_userId = view.findViewById<TextView>(R.id.ready_userId)
        val ready_userLevel = view.findViewById<TextView>(R.id.ready_userLevel)
    }
}
