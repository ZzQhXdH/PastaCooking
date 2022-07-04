package com.hontech.pastacooking.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.fragment.sub.AdjWeightDelegate
import com.hontech.pastacooking.activity.fragment.sub.WeightStatusDelegate
import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.event.WeightStatusEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WeightFragment : Fragment() {

    private lateinit var mStatus: WeightStatusDelegate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weight, null)
        init(view)
        return view
    }

    private fun init(view: View) {
        AdjWeightDelegate(view)
        mStatus = WeightStatusDelegate(view)
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onStatusEvnet(env: WeightStatusEvent) {
        mStatus.update()
    }

    override fun onStart() {
        super.onStart()
        bus.register(this)
    }

    override fun onStop() {
        super.onStop()
        bus.unregister(this)
    }
}