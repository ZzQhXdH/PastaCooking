package com.hontech.pastacooking.activity.window

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.app.dimenById
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val width = dimenById(R.dimen.y400)
private val height = dimenById(R.dimen.x210)

class DialogWindow : MoveableWindow(R.layout.window_dialog, width, height) {

    private val btnOk = view.findViewById<Button>(R.id.id_btn_ok)
    private val btnCancel = view.findViewById<Button>(R.id.id_btn_cancel)
    private val tvMsg = view.findViewById<TextView>(R.id.id_text_view_msg)

    private var cont: Continuation<Unit>? = null

    init {
        btnOk.setOnClickListener {
            cont?.resume(Unit)
            dismiss()
        }

        btnCancel.setOnClickListener {
            cont?.resumeWithException(IllegalStateException("取消"))
            dismiss()
        }
    }

    fun show(parent: View, text: String, cont: Continuation<Unit>) {
        show(parent)
        tvMsg.text = text
        this.cont = cont
    }


}

suspend fun showDialog(parent: View, text: String) = suspendCoroutine<Unit> {
    DialogWindow().show(parent, text, it)
}

