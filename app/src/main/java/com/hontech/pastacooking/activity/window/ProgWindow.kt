package com.hontech.pastacooking.activity.window

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.app.AppExecutor
import com.hontech.pastacooking.app.dimenById

private val width = dimenById(R.dimen.x250)
private val height = dimenById(R.dimen.y300)

class ProgWindow : MoveableWindow (R.layout.window_prog, width, height) {

    private val btnClose = view.findViewById<Button>(R.id.id_btn_close)
    private val image = view.findViewById<ImageView>(R.id.id_img_prog)
    private val prog = view.findViewById<ProgressBar>(R.id.id_prog)
    private val textView = view.findViewById<TextView>(R.id.id_text_view_msg)

    init {
        btnClose.setOnClickListener { dismiss() }
    }

    private fun setOk() {
        prog.visibility = View.INVISIBLE
        image.visibility = View.VISIBLE
        image.setImageResource(R.drawable.ic_succ)
    }

    private fun setErr() {
        prog.visibility = View.INVISIBLE
        image.visibility = View.VISIBLE
        image.setImageResource(R.drawable.ic_err)
    }

    private fun setProg() {
        prog.visibility = View.VISIBLE
        image.visibility = View.INVISIBLE
    }

    fun error(text: String) {
        setErr()
        textView.text = text
    }

    fun success(text: String) {
        setOk()
        textView.text = text
        AppExecutor.postDelayed({dismiss()}, 2 * 1000L)
    }

    fun show(parent: View, text: String) {
        setProg()
        textView.text = text
        show(parent)
    }
}

fun showProg(parent: View, text: String): ProgWindow {
    val w = ProgWindow()
    w.show(parent, text)
    return w
}












