package com.hontech.pastacooking.utils

import android.os.Environment
import java.io.File
import java.io.FileOutputStream


fun saveFile(body: ByteArray, name: String): String {
    val dir = Environment.getExternalStorageDirectory()
    val file = File(dir, name)
    file.createNewFile()
    val fos = FileOutputStream(file)
    fos.write(body)
    fos.flush()
    fos.close()
    return file.path
}

