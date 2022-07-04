package com.hontech.pastacooking.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.fragment.sub.*
import com.hontech.pastacooking.activity.view.LogView
import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.conn.HeaterProto
import com.hontech.pastacooking.event.HeatExceptEvent
import com.hontech.pastacooking.event.HeaterStatusEvent
import com.hontech.pastacooking.event.WeightStatusEvent
import com.hontech.pastacooking.ext.toHex16
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HeaterFragment : Fragment() {

    private lateinit var mValve: ValveDelegate
    private lateinit var mPump: PumpDelegate
    private lateinit var mHeater: HeaterDelegate
    private lateinit var mStatus: HeaterSensorDelegate
    private lateinit var logView: LogView
    private lateinit var mOther: HeaterOtherDelegate
    private lateinit var mHeaterBucketStatusDelegate: HeaterBucketStatusDelegate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_heater, null)
        init(view)
        return view
    }

    private fun init(view: View) {
        mValve = ValveDelegate(view)
        mPump = PumpDelegate(view)
        mHeater = HeaterDelegate(view)
        mStatus = HeaterSensorDelegate(view)
        NozzleDelegate(view)
        DrawDelegate(view)
        FlowDelegate(view)
        WaterParamDelegate(view)
        SteamParamDelegate(view)
        DrawParamDelegate(view)
        CookingDelegate(view)
        CleanDelegate(view)
        mHeaterBucketStatusDelegate = HeaterBucketStatusDelegate(view)
        logView = view.findViewById(R.id.id_log_view)
        mOther = HeaterOtherDelegate(view)
    }

    override fun onStart() {
        super.onStart()
        bus.register(this)
    }

    override fun onStop() {
        super.onStop()
        bus.unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onHeaterStatusEvent(env: HeaterStatusEvent) {
        mValve.update(HeaterProto.status)
        mPump.update(HeaterProto.status)
        mHeater.update(HeaterProto.status)
        mStatus.update(HeaterProto.status)
        mOther.update(HeaterProto.status)
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onWeightStatusEvent(env: WeightStatusEvent) {
        mHeaterBucketStatusDelegate.update()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onHeatExceptEvent(env: HeatExceptEvent) {
        logView.append("code:${env.ec.toHex16()} ${env.msg}")
    }
}


