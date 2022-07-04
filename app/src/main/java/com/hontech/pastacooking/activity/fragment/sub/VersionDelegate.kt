package com.hontech.pastacooking.activity.fragment.sub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showErr
import com.hontech.pastacooking.app.AppScope
import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.app.installApp
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.event.DownloadProgEvent
import com.hontech.pastacooking.ext.formatDate
import com.hontech.pastacooking.model.FirmModel
import com.hontech.pastacooking.utils.Http
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private class ItemView(view: View) : RecyclerView.ViewHolder(view) {

    private val tvVersion = view.findViewById<TextView>(R.id.id_tv_version)
    private val tvName = view.findViewById<TextView>(R.id.id_tv_name)
    private val tvSize = view.findViewById<TextView>(R.id.id_tv_size)
    private val btnUpdate = view.findViewById<TextView>(R.id.id_btn_update)
    private val tvProg = view.findViewById<TextView>(R.id.id_tv_prog)
    private val tvDate = view.findViewById<TextView>(R.id.id_tv_date)

    fun setUp(firm: FirmModel) {
        tvVersion.text = firm.version
        tvName.text = firm.name
        tvSize.text = "${firm.size}Byte"
        tvDate.text = firm.timestamp.formatDate()
        btnUpdate.setOnClickListener {
            AppScope.launch { update(firm) }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onProg(env: DownloadProgEvent) {
        tvProg.text = "${env.prog}%"
    }

    suspend fun update(firm: FirmModel) {

        try {
            bus.register(this)
            val path = Http.download(firm.id, "pasta.apk")
            installApp(path)
        } catch (e: Exception) {
            showErr("下载失败", btnUpdate)
        } finally {
            bus.unregister(this)
        }
    }
}

private class Adapter(var firms: Array<FirmModel>) : RecyclerView.Adapter<ItemView>() {

    fun change(firms: Array<FirmModel>) {
        this.firms = firms
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        val firm = firms[position]
        holder.setUp(firm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ipc_version, parent, false)
        return ItemView(view)
    }

    override fun getItemCount(): Int {
        return firms.size
    }
}

class VersionDelegate(view: View) {

    private val bk = view.findViewById<View>(R.id.id_soft_list_main)
    private val btnGet = bk.findViewById<View>(R.id.id_btn_get)
    private val rv = bk.findViewById<RecyclerView>(R.id.id_soft_list_rv)
    private val tv = bk.findViewById<TextView>(R.id.id_soft_list_tv_title)

    private val adapter = Adapter(emptyArray())

    init {
        tv.text = "工控机软件信息"
        btnGet.onClick(::onClickGet)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context, LinearLayoutManager.VERTICAL, false)
    }

    private suspend fun onClickGet() {

        try {
            adapter.change(emptyArray())
            val firms = Http.getIpcFirm()
            adapter.change(firms)
        } catch (e: Exception) {
            showErr("获取失败", btnGet)
        }
    }
}

