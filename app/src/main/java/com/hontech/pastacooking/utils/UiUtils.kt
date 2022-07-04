package com.hontech.pastacooking.utils

import android.view.View

@Retention
annotation class UiBind(val id: Int)


fun View.bindViews(any: Any) {

    val fields = any.javaClass.declaredFields
    for (field in fields) {
        val bind = field.getAnnotation(UiBind::class.java)
        if (bind == null) {
            continue
        }
        field.isAccessible = true
        val view = this.findViewById<View>(bind.id)
        field.set(any, view)
    }
}

