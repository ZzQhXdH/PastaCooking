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
import com.hontech.pastacooking.event.ScanBarcodeEvent
import com.hontech.pastacooking.task.conn.scan
import com.hontech.pastacooking.task.conn.testScanner
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ScanDelegate(view: View) {

    private val btnTest = view.findViewById<Button>(R.id.id_btn_scan_test)
    private val btnAll = view.findViewById<Button>(R.id.id_btn_scan_all)
    private val rvBarcode = view.findViewById<RecyclerView>(R.id.id_rv_bar_code)
    private val adapter = Adapter()

    init {
        btnTest.onClick(::test)
        btnAll.onClick(::scanAll)
        rvBarcode.adapter = adapter
        rvBarcode.layoutManager =
            LinearLayoutManager(btnTest.context, LinearLayoutManager.VERTICAL, false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onBarcodeEvent(env: ScanBarcodeEvent) {
        val size = adapter.size()
        adapter.add(env)
        rvBarcode.smoothScrollToPosition(size)
    }

    private suspend fun scanAll() {

        val w = showProg(btnTest, "扫描库存")
        adapter.clear()
        bus.register(this)
        try {
            scan()
            w.success("扫描成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        } finally {
            bus.unregister(this)
        }
    }

    private suspend fun test() {

        val w = showProg(btnTest, "测试扫码枪")
        try {
            val code = testScanner()
            w.success("成功:$code")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private class ItemView(view: View) : RecyclerView.ViewHolder(view) {

        private val tvCargo = view.findViewById<TextView>(R.id.id_item_tv_cargo)
        private val tvBarcode = view.findViewById<TextView>(R.id.id_item_tv_barcode)

        fun setUp(env: ScanBarcodeEvent) {
            val cargo = "${env.col}-${env.row}"
            if (env.err != 0) {
                tvBarcode.text = env.errString()
            } else {
                tvBarcode.text = env.barcode
            }
            tvCargo.text = cargo
        }
    }

    private class Adapter : RecyclerView.Adapter<ItemView>() {

        private val cargos = ArrayList<ScanBarcodeEvent>()

        fun clear() {
            val count = cargos.size
            cargos.clear()
            notifyItemRangeRemoved(0, count)
        }

        fun size() = cargos.size

        fun add(env: ScanBarcodeEvent) {
            val count = cargos.size
            cargos.add(env)
            notifyItemInserted(count)
        }

        override fun getItemCount(): Int {
            return cargos.size
        }

        override fun onBindViewHolder(holder: ItemView, position: Int) {
            val env = cargos[position]
            holder.setUp(env)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scan_barcode, parent, false)
            return ItemView(view)
        }
    }

}