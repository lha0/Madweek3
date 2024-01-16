package com.example.madweek3

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
class AnswerDialog(context: Context) {
    private val dlg = Dialog(context)
    private lateinit var answerBtn: Button
    private lateinit var answerEditText: EditText
    private lateinit var Alistener: DialogAnswerClickedListener

    fun start() {
        dlg.setContentView(R.layout.dialog_answer)
        dlg.setCancelable(false)

        answerEditText = dlg.findViewById(R.id.dialogAnswerEditText)

        answerBtn = dlg.findViewById(R.id.dialogAnswerBtn)
        answerBtn.setOnClickListener {
            val userInput = answerEditText.text.toString()
            //정답 입력하기 클릭했을 때 실행할 내용
            Alistener.onAnswerClicked(userInput)
        }
        dlg.show()
    }

    fun setOnAnswerClickedListener(listener: (String) -> Unit) {
        this.Alistener = object: DialogAnswerClickedListener {
            override fun onAnswerClicked(content: String) {
                println("onAnswerClicked " + content)
                listener(content)
            }
        }
    }

    interface DialogAnswerClickedListener {
        fun onAnswerClicked(content: String)
    }

    fun dismissDialog() {
        dlg.dismiss()
    }
}