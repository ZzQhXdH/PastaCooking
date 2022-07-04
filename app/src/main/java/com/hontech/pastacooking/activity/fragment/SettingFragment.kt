package com.hontech.pastacooking.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.fragment.sub.DeviceOtaDelegate
import com.hontech.pastacooking.activity.fragment.sub.LoginViewDelegate
import com.hontech.pastacooking.activity.fragment.sub.VersionDelegate
import com.hontech.pastacooking.activity.view.LogView
import com.hontech.pastacooking.activity.window.showErr
import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.app.portNames
import com.hontech.pastacooking.conn.Device
import com.hontech.pastacooking.conn.HeaterProto
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.conn.WeightProto
import com.hontech.pastacooking.event.HeaterStatusEvent
import com.hontech.pastacooking.event.LoginChangedEvent
import com.hontech.pastacooking.event.SystemErrEvent
import com.hontech.pastacooking.event.WeightStatusEvent
import com.hontech.pastacooking.ext.MainStatusEvent
import com.hontech.pastacooking.ext.toHex16
import org.angmarch.views.NiceSpinner
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SettingFragment : Fragment() {

    private lateinit var mSpinnerPort: NiceSpinner
    private lateinit var mBtnOpen: Button
    private lateinit var mTextMainVersion: TextView
    private lateinit var mTextHeatorVersion: TextView
    private lateinit var mTextWeightVersion: TextView
    private lateinit var mLoginView: LoginViewDelegate
    private lateinit var mLogView: LogView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, null)
        init(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        bus.register(this)
    }

    override fun onStop() {
        super.onStop()
        bus.unregister(this)
    }

    private fun configPort() {
        mSpinnerPort.attachDataSource(portNames)
    }

    private fun init(view: View) {
        mSpinnerPort = view.findViewById(R.id.id_spinner)
        mBtnOpen = view.findViewById(R.id.id_btn_open)
        configPort()
        mBtnOpen.setOnClickListener(onClickPortOpen)
        mTextMainVersion = view.findViewById(R.id.id_text_view_main_version)
        mTextHeatorVersion = view.findViewById(R.id.id_text_view_heator_version)
        mTextWeightVersion = view.findViewById(R.id.id_text_view_weight_version)
        mLogView = view.findViewById(R.id.id_setting_log_view)
        VersionDelegate(view)
        DeviceOtaDelegate(view)

        mLoginView = LoginViewDelegate(view)
        mLoginView.update()

        onClickPortOpen(mBtnOpen)
    }

    private val onClickPortOpen = fun(_: View) {

        if (Device.isOpen()) {
            Device.close()
            mBtnOpen.text = "打开"
            return
        }

        val index = mSpinnerPort.selectedIndex
        val name = portNames[index]

        try {
            Device.open(name)
            mBtnOpen.text = "关闭"
        } catch (e: Exception) {
            e.printStackTrace()
            showErr("打开串口失败", mBtnOpen)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onMainStatusEvent(env: MainStatusEvent) {
        val version = MainProto.status.appVersion.value.toHex16()
        mTextMainVersion.text = "主控板软件版本:$version"
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onHeaterStatusEvent(env: HeaterStatusEvent) {
        val version = HeaterProto.status.appVersion.value.toHex16()
        mTextHeatorVersion.text = "加热板软件版本:${version}"
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onWeightStatusEvent(env: WeightStatusEvent) {
        val version = WeightProto.status.appVersion.value.toHex16()
        mTextWeightVersion.text = "称重板软件版本:$version"
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onLoginChangedEvent(env: LoginChangedEvent) {
        mLoginView.update()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onSystemErrEvent(env: SystemErrEvent) {
        mLogView.append(env.msg)
    }

}

