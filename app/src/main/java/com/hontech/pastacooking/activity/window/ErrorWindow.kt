package com.hontech.pastacooking.activity.window

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.app.dimenById

private val width = dimenById(R.dimen.y320)
private val height = dimenById(R.dimen.x320)

class ErrorWindow : MoveableWindow(R.layout.window_err, width, height) {

    private val mBtnClose = view.findViewById<Button>(R.id.id_btn_close)
    private val mTextInfo = view.findViewById<TextView>(R.id.id_text_view_msg)

    init {
        mBtnClose.setOnClickListener { dismiss() }
    }

    fun show(parent: View, info: String) {
        super.show(parent)
        mTextInfo.text = info
    }
}

fun showErr(info: String, parent: View) {
    ErrorWindow().show(parent, info)
}


