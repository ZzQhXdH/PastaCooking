package com.hontech.pastacooking.activity.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.model.CmdStatus

class CmdStatusView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mListView: RecyclerView
    private val mClearBtn: Button
    private val mAdapter = Adapter()

    private val onClickClear = fun(_: View) {
        clear()
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_cmd_status, this)
        mListView = view.findViewById(R.id.id_recycler_view_list)
        mClearBtn = view.findViewById(R.id.id_btn_clear)
        mClearBtn.setOnClickListener(onClickClear)
        mListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        mListView.adapter = mAdapter
    }

    fun clear() {
        mAdapter.clear()
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val mProg = view.findViewById<ProgressBar>(R.id.id_item_prog)
        private val mImg = view.findViewById<ImageView>(R.id.id_item_img)
        private val mTitle = view.findViewById<TextView>(R.id.id_item_text_view_title)
        private val mResult = view.findViewById<TextView>(R.id.id_item_text_view_result)

        fun setUp(cmd: CmdStatus) {
            mTitle.text = cmd.titleInfo()
            val progress = cmd.progress()
            when (progress) {
                CmdStatus.Prog -> {
                    mResult.text = "正在执行中"
                    mImg.visibility = View.INVISIBLE
                    mProg.visibility = View.VISIBLE
                }

                CmdStatus.Ok -> {
                    mProg.visibility = View.INVISIBLE
                    mResult.text = cmd.result()
                    mImg.setImageResource(R.drawable.ic_ok)
                }

                CmdStatus.Fail -> {
                    mProg.visibility = View.INVISIBLE
                    mResult.text = cmd.result()
                    mImg.setImageResource(R.drawable.ic_fail)
                }
            }

        }
    }

    private class Adapter : RecyclerView.Adapter<ViewHolder>() {

        private val mCmds = ArrayList<CmdStatus>()

        fun clear() {
            val size = mCmds.size
            mCmds.clear()
            notifyItemRangeRemoved(0, size)
        }

        fun addCmd(cmd: CmdStatus) {
            mCmds.add(cmd)
            val size = mCmds.size
            notifyItemInserted(size - 1)
        }

        override fun getItemCount(): Int {
            return mCmds.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val cmd = mCmds[position]
            cmd.setNotify { notifyItemChanged(position) }
            holder.setUp(cmd)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cmd_status, parent)
            return ViewHolder(view)
        }
    }


}