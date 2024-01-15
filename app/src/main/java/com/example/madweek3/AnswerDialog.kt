package com.example.madweek3

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
class AnswerDialog(context: Context) {
    private val dlg = Dialog(context)
    private lateinit var dialogContent : TextView
    private lateinit var answerBtn: Button
    private lateinit var qBtn : Button
    private lateinit var Alistener: DialogAnswerClickedListener
    private lateinit var Qlistener: DialogQClickedListner

    fun start(content: String) {
        dlg.setContentView(R.layout.dialog_answer)
        dlg.setCancelable(false)

        dialogContent = dlg.findViewById(R.id.dialogContent)
        dialogContent.text = content

        answerBtn = dlg.findViewById(R.id.dialogAnswerBtn)
        answerBtn.setOnClickListener {
            //정답 입력하기 클릭했을 때 실행할 내용
            Alistener.onAnswerClicked("정답을 입력했습니다.")
            dlg.dismiss()
        }

        qBtn = dlg.findViewById(R.id.dialogQBtn)
        qBtn.setOnClickListener {
            //질문하기 클릭했을 때 실행할 내용
            Qlistener.onQuestionClicked("질문하기를 눌렀습니다.")
            dlg.dismiss()
        }

        dlg.show()
    }

    fun setOnAnswerClickedListener(listener: (String) -> Unit) {
        this.Alistener = object: DialogAnswerClickedListener {
            override fun onAnswerClicked(content: String) {
                listener(content)
            }
        }
    }

    fun setOnQuestionClickedListener(listener: (String) -> Unit) {
        this.Qlistener = object: DialogQClickedListner {
            override fun onQuestionClicked(content: String) {
                listener(content)
            }
        }
    }

    interface DialogAnswerClickedListener {
        fun onAnswerClicked(content: String)
    }

    interface DialogQClickedListner {
        fun onQuestionClicked(content: String)
    }

}