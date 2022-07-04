package com.hontech.pastacooking.activity.fragment.sub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.event.OtaProgEvent
import com.hontech.pastacooking.ext.formatDate
import com.hontech.pastacooking.model.FirmModel
import com.hontech.pastacooking.task.conn.OtaCtx
import com.hontech.pastacooking.task.conn.runOta
import com.hontech.pastacooking.utils.Http
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FirmDelegate(view: View, rvId: Int, btnId: Int, val type: String, val ctx: OtaCtx) {

    private val rv = view.findViewById<RecyclerView>(rvId)
    private val btnGet = view.findViewById<Button>(btnId)
    private val adapter = Adapter(ctx)

    init {
        btnGet.onClick(::onClickGet)

        rv.layoutManager = LinearLayoutManager(rv.context, LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }

    private suspend fun onClickGet() {

        val w = showProg(btnGet, "获取固件")
        try {
            val firms = Http.getFirm(type)
            adapter.update(firms)
            w.success("获取固件成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private class ItemView(view: View) : RecyclerView.ViewHolder(view) {

        private val tvVersion = view.findViewById<TextView>(R.id.id_tv_version)
        private val tvName = view.findViewById<TextView>(R.id.id_tv_name)
        private val tvSize = view.findViewById<TextView>(R.id.id_tv_size)
        private val tvDate = view.findViewById<TextView>(R.id.id_tv_date)
        private val tvProg = view.findViewById<TextView>(R.id.id_tv_prog)
        private val btnUpdate = view.findViewById<Button>(R.id.id_btn_update)

        private var firm: FirmModel? = null
        private var ctx: OtaCtx? = null

        fun setUp(firm: FirmModel, ctx: OtaCtx) {
            tvVersion.text = firm.version
            tvName.text = firm.name
            tvSize.text = "${firm.size}"
            tvDate.text = firm.timestamp.formatDate()
            tvProg.text = "0"
            btnUpdate.onClick(::onClickUpdate)
            this.firm = firm
            this.ctx = ctx
        }

        @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
        fun onOtaProgEvent(env: OtaProgEvent) {
            if ((ctx == null) || (ctx!!.addr != env.addr)) {
                return
            }
            tvProg.text = "进度:${env.prog}%"
        }

        private suspend fun onClickUpdate() {

            if ((firm == null) || (ctx == null)) {
                return
            }

            val w = showProg(btnUpdate, "下载固件")
            bus.register(this)
            try {
                val data = Http.downloadSuspendFirm(firm!!.id)
                w.show(btnUpdate, "下载成功 开始更新")
                runOta(ctx!!, data)
                w.success("更新成功")
            } catch (e: Exception) {
                e.printStackTrace()
                w.error(e.message!!)
            } finally {
                bus.unregister(this)
            }
        }
    }

    private class Adapter(val ctx: OtaCtx) : RecyclerView.Adapter<ItemView>() {

        private var firms = emptyArray<FirmModel>()

        fun update(firms: Array<FirmModel>) {
            notifyItemRangeRemoved(0, this.firms.size)
            this.firms = firms
            notifyItemRangeInserted(0, firms.size)
        }

        override fun getItemCount(): Int {
            return firms.size
        }

        override fun onBindViewHolder(holder: ItemView, position: Int) {
            val firm = firms[position]
            holder.setUp(firm, ctx)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ipc_version, parent, false)
            return ItemView(view)
        }
    }
}