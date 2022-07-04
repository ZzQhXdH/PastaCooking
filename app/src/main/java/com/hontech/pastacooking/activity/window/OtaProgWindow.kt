package com.hontech.pastacooking.activity.window

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.app.AppTask
import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.app.dimenById
import com.hontech.pastacooking.event.OtaCompleteEvent
import com.hontech.pastacooking.event.OtaProgEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private val width = dimenById(R.dimen.x240)
private val height = dimenById(R.dimen.y320)

class OtaProgWindow : MoveableWindow(R.layout.window_ota_prog, width, height) {

    private val btnClose = view.findViewById<Button>(R.id.id_btn_close)

    private val prog = view.findViewById<ProgressBar>(R.id.id_prog)
    private val tvProg = view.findViewById<TextView>(R.id.id_tv_prog)
    private val tvTitle = view.findViewById<TextView>(R.id.id_tv_title)
    private val img = view.findViewById<ImageView>(R.id.id_image)

    init {
        btnClose.setOnClickListener { dismiss() }
        bus.register(this)
        onCloseFn = { bus.unregister(this) }
    }

    private fun showProg() {
        prog.visibility = View.VISIBLE
        img.visibility = View.INVISIBLE
    }

    private fun showErr() {
        prog.visibility = View.INVISIBLE
        img.visibility = View.VISIBLE
        img.setImageResource(R.drawable.ic_err)
    }

    private fun showSucc() {
        prog.visibility = View.INVISIBLE
        img.visibility = View.VISIBLE
        img.setImageResource(R.drawable.ic_ok)
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onOtaProgEvent(env: OtaProgEvent) {
        tvProg.text = "进度:${env.prog}"
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onOtaCompleteEvent(env: OtaCompleteEvent) {
        if (env.succ) {
            showSucc()
        } else {
            showErr()
        }
        AppTask.postDelayed({ dismiss() }, 30 * 1000L)
    }

    fun show(parent: View, title: String) {
        super.show(parent)
        showProg()
        tvTitle.text = title
        tvProg.text = ""
    }
}





