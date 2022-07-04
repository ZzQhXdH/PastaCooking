package com.hontech.pastacooking.event

class OtaStartEvent(val title: String)

class OtaProgEvent(val addr: Int, val prog: Int)

class OtaCompleteEvent(val succ: Boolean)

