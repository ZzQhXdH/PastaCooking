package com.hontech.pastacooking.activity.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.app.AppTask

class LogView : FrameLayout {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val scrollView: ScrollView
    private val textView: TextView
    private val btnClear: Button

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_log, this)
        scrollView = view.findViewById(R.id.id_scroll_view)
        textView = view.findViewById(R.id.id_text_view)
        btnClear = view.findViewById(R.id.id_btn_clear)
        btnClear.setOnClickListener { clear() }
    }

    fun clear() {
        textView.text = ""
        AppTask.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    fun append(text: String) {
        textView.append("${text}\r\n")
        AppTask.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}