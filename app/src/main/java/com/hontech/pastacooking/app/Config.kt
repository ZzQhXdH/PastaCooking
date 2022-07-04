package com.hontech.pastacooking.app


val portNames = listOf("/dev/ttyS4", "/dev/ttyS3", "/dev/ttyS2", "/dev/ttyS1", "/dev/ttyS0")

const val ServeAddr = "106.14.180.8"
const val WebPort = 22331
const val DevPort = 22332

const val WebBase = "http://$ServeAddr:$WebPort"

val PackageName = AppContext.packageName
val Version = AppContext.packageManager.getPackageInfo(PackageName, 0).versionName
