package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.net.Client

class LoginViewDelegate(view: View) {

    private val tvMac = view.findViewById<TextView>(R.id.id_tv_mac_addr)
    private val tvVer = view.findViewById<TextView>(R.id.id_tv_version)
    private val tvName = view.findViewById<TextView>(R.id.id_tv_device_name)
    private val tvCity = view.findViewById<TextView>(R.id.id_tv_city)

    fun update() {
        tvMac.text = Client.info.macAddr
        tvVer.text = Client.info.version
        tvName.text = Client.info.name
        tvCity.text = Client.info.city
    }
}