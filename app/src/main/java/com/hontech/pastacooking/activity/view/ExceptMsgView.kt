package com.hontech.pastacooking.activity.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hontech.pastacooking.R

class ExceptMsgView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mTitleView: TextView
    private val mListView: RecyclerView
    private val mClearBtn: Button
    private val mAdapter = Adapter()

    private val onClickClear = fun(_: View) {
        clear()
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_except_msg, this)
        mTitleView = view.findViewById(R.id.id_text_view_title)
        mListView = view.findViewById(R.id.id_recycler_view_list)
        mClearBtn = view.findViewById(R.id.id_btn_clear)
        mClearBtn.setOnClickListener(onClickClear)
        mListView.adapter = Adapter()
        mListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun setTitle(title: String) {
        mTitleView.text = title
    }

    fun clear() {
        mAdapter.clear()
    }

    fun addMsg(msg: String) {
        mAdapter.addMsg(msg)
    }

    private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val mMsgView = view.findViewById<TextView>(R.id.id_item_msg)

        fun setUp(msg: String) {
            mMsgView.text = msg
        }
    }

    private class Adapter : RecyclerView.Adapter<ViewHolder>() {

        private val msgList = ArrayList<String>()

        fun clear() {
            notifyItemRangeRemoved(0, msgList.size)
            msgList.clear()
        }

        fun addMsg(msg: String) {
            msgList.add(msg)
            notifyItemInserted(msgList.size - 1)
        }

        override fun getItemCount(): Int {
            return msgList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_except_msg, parent)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setUp(msgList[position])
        }
    }

}