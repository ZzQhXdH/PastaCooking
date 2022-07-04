package com.hontech.pastacooking.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hontech.pastacooking.activity.MainActivity

class InstallApkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if ((context == null) || (intent == null)) {
            return
        }
        if (intent.action == Intent.ACTION_PACKAGE_REPLACED) {
            onReplace(context)
        }
    }

    private fun onReplace(context: Context) {
        val i = Intent(context, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }
}

