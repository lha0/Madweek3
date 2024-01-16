package com.example.madweek3

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import android.widget.Toast

interface EnterPasswordDialogListener {
    fun onPasswordEntered(password: String)
}

class EnterPasswordDialog : DialogFragment() {

    private var onItemClick: ((String) -> Unit)? = null

    // 외부에서 클릭 이벤트 처리 함수를 설정할 수 있는 메서드
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClick = listener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_enter_password, null)
        builder.setView(dialogView)

        val passwordEditText = dialogView.findViewById<EditText>(R.id.passwordEditText)
        val confirmButton = dialogView.findViewById<Button>(R.id.confirmButton_toEnterPassword)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton_toEnterPassword)


        val dialog = builder.create()
        val displayMetrics = resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels

        // Dialog 크기 조절
        val width = (widthPixels * 0.8).toInt()
        val height = (heightPixels * 0.8).toInt()
        dialog.window?.setLayout(width, height)

        confirmButton.setOnClickListener {

            val enteredPassword = passwordEditText.text.toString()
            onItemClick?.invoke(enteredPassword)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }


}