package com.example.madweek3

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.content.Context
import android.view.Window
import android.view.WindowManager

class LevelExplainFragment(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        val dialog = super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.fragment_level_explain)

        val params: WindowManager.LayoutParams = window!!.attributes
        params.width = (context.resources.displayMetrics.widthPixels * 0.9).toInt() // 디스플레이 너비의 0.9배
        params.height = WindowManager.LayoutParams.WRAP_CONTENT // 높이는 필요에 따라 조정
        window?.attributes = params
        return dialog

//        val closeButton = findViewById<Button>(R.id.closeButton)
//        closeButton.setOnClickListener {
//            dismiss() // 다이얼로그를 닫습니다.
//        }
    }
}