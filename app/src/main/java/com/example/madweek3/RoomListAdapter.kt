package com.example.madweek3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.madweek3.APIservice

class RoomListAdapter(private val context: Context, private var dataList: List<Room>) :
    RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {

    private var onItemClick: ((Room) -> Unit)? = null

    // 외부에서 클릭 이벤트 처리 함수를 설정할 수 있는 메서드
    fun setOnItemClickListener(listener: (Room) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.room_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.roomName.text = data.roomName
        holder.numPeople.text = data.numPeople.toString()
        val privateBoolean = data.privateLock
        if (privateBoolean) {
            holder.privateSetButton.setImageResource(R.drawable.lock_icon)
        }
        else {
            holder.privateSetButton.setImageResource(R.drawable.unlock_icon)
        }

        //사용자가 원하는 방 클릭하면 ready fragment로 전환 & 해당 방ID 함께 넘겨주기.

        holder.cardView.setOnClickListener {
            onItemClick?.invoke(data)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.RoomList_cardView_item)
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val numPeople: TextView = itemView.findViewById(R.id.numPeople)
        val privateSetButton: ImageView = itemView.findViewById(R.id.privateSetButton)
    }


    fun updateData(newDataList: List<Room>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}