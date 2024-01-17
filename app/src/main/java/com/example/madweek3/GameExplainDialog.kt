package com.example.madweek3

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class GameExplainDialog(context: Context) : Dialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        val dialog = super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_game_explain)

        val params: WindowManager.LayoutParams = window!!.attributes
        params.width = (context.resources.displayMetrics.widthPixels * 0.9).toInt() // 디스플레이 너비의 0.9배
        params.height = WindowManager.LayoutParams.WRAP_CONTENT // 높이는 필요에 따라 조정
        window?.attributes = params
        return dialog
    }


}