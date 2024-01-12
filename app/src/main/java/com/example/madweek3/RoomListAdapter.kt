package com.example.madweek3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.madweek3.APIservice

class RoomListAdapter(private val context: Context, private val dataList: List<Room>) :
    RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {

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
}