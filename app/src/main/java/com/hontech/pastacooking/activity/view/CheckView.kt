package com.hontech.pastacooking.activity.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.hontech.pastacooking.R


class CheckView : FrameLayout {

    private val mText: TextView
    private val mView: View

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_check, this)
        mText = view.findViewById(R.id.id_check_text_view)
        mView = view.findViewById(R.id.id_check_view)


        val attr = context.obtainStyledAttributes(attrs, R.styleable.CheckView)
        val text = attr.getText(R.styleable.CheckView_text)
        setText(text.toString())


        attr.recycle()
    }


    fun setText(text: String) {
        mText.text = text
    }

    fun setChecked(flag: Boolean) {
        if (flag) {
            mView.setBackgroundResource(R.drawable.shape_red)
        } else {
            mView.setBackgroundResource(R.drawable.shape_gray)
        }
    }

}