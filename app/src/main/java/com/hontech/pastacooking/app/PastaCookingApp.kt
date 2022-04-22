package com.hontech.pastacooking.app

import android.app.Application
import android.content.Context
import android.content.ContextWrapper

private lateinit var INSTANCE: Context

object AppContext : ContextWrapper(INSTANCE)

class PastaCookingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}