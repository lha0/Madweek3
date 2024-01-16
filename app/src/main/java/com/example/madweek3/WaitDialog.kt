package com.example.madweek3

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
class WaitDialog(context: Context) {
    private val dlg = Dialog(context)
    private lateinit var closeBtn : Button

    fun start() {
        dlg.setContentView(R.layout.dialog_wait)
        dlg.setCancelable(false)



        closeBtn = dlg.findViewById(R.id.closeBtn)

        closeBtn.setOnClickListener {
            dismissDialog()
        }

        dlg.show()
    }

    fun dismissDialog() {
        dlg.dismiss()
    }

}