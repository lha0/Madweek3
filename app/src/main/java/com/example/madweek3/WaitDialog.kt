package com.example.madweek3

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
class WaitDialog(context: Context) {
    private val dlg = Dialog(context)
    private lateinit var waitDialogTV : TextView

    fun start() {
        dlg.setContentView(R.layout.dialog_wait)
        dlg.setCancelable(false)

        dlg.show()
    }

    fun dismissDialog() {
        dlg.dismiss()
    }

}