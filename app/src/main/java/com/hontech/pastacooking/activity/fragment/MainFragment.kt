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
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.event.CookProgEvent
import com.hontech.pastacooking.event.FridgeStatusEvent
import com.hontech.pastacooking.event.MainExceptEvent
import com.hontech.pastacooking.ext.MainStatusEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainFragment : Fragment() {

    private lateinit var mainSensor: MainSensor
    private lateinit var mainStatusTv: MainStatusTv
    private lateinit var logView: LogView
    private lateinit var mFridge: FridgeDelegate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, null)
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

    private fun init(view: View) {
        mainSensor = MainSensor(view)
        mainStatusTv = MainStatusTv(view)
        CargoRotateDelegate(view)
        CargoElevatorDelegate(view)
        CargoFetchDelegate(view)
        HeatDoorDelegate(view)
        ExternPushDelegate(view)
        PastaDoorDelegate(view)
        PickDoorDelegate(view)
        ScanDelegate(view)
        TestPickupDelegate(view)
        PickUpCookingDelegate(view)
        CleanParamDelegate(view)
        LedDelegate(view)
        mFridge = FridgeDelegate(view)
        logView = view.findViewById(R.id.id_log_view)
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onMainStatusEvent(env: MainStatusEvent) {
        mainSensor.update(MainProto.status)
        mainStatusTv.update(MainProto.status)
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onFridgeStatusEvent(env: FridgeStatusEvent) {
        mFridge.update()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onMainExceptEvent(env: MainExceptEvent) {
        logView.append("code:${env.ec} ${env.msg}")
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onProgEvent(env: CookProgEvent) {
        logView.append("进度:${env.msg}")
    }

}

