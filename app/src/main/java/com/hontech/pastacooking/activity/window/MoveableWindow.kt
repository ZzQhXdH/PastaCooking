package com.hontech.pastacooking.activity.window

import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import com.hontech.pastacooking.R
import com.hontech.pastacooking.app.AppContext

open class MoveableWindow (viewId: Int, val width: Int, val height: Int) {

    protected val view = LayoutInflater.from(AppContext).inflate(viewId, null)
    protected val window = PopupWindow(view, width, height, false)

    var isShow = false
        private set

    private var mDownX = 0
    private var mDownY = 0
    private var mX = 0
    private var mY = 0

    private val onDismiss = fun() {
        isShow = false
    }

    private val onTouch = fun(_: View, env: MotionEvent): Boolean {

        when (env.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = env.rawX.toInt()
                mDownY = env.rawY.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                val x = env.rawX.toInt()
                val y = env.rawY.toInt()
                val moveX = x - mDownX
                val moveY = y - mDownY
                mX += moveX
                mY += moveY
                mDownX = x
                mDownY = y
                window.update(mX, mY, -1, -1, true)
            }
        }

        return true
    }


    init {
        window.isOutsideTouchable = false
        window.setOnDismissListener(onDismiss)
        view.setOnTouchListener(onTouch)
        window.animationStyle = R.style.PopupTheme
    }

    fun show(parent: View) {
        if (isShow) {
            return
        }
        isShow = true
        window.showAtLocation(parent, Gravity.CENTER, mX, mY)
    }

    fun dismiss() {
        if (!isShow) {
            return
        }
        window.dismiss()
    }
}