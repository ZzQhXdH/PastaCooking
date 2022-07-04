package com.hontech.pastacooking.utils

import android.os.Environment
import com.hontech.pastacooking.app.*
import com.hontech.pastacooking.event.DownloadProgEvent
import com.hontech.pastacooking.model.FirmModel
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object Http {

    const val firmUrl = "$WebBase/api/firm/download"
    const val baseURL = "$WebBase/api"

    const val FileTypeIpc = "ipc"
    const val FileTypeMain = "main"
    const val FileTypeHeater = "heater"
    const val FileTypeWeighter = "weighter"

    private val client = OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    fun get(url: String): ByteArray {
        val req = Request.Builder()
            .url(url)
            .get()
            .build()
        val res = client.newCall(req).execute()
        val body = res.body
        if (body == null) {
            throw IOException("网络异常")
        }
        return body.bytes()
    }

    suspend fun downloadSuspendFirm(id: Int) = suspendCoroutine<ByteArray> {

        NetTask.post {
            try {
                val body = get("${firmUrl}?id=$id")
                it.resume(body)
            } catch (e: Exception) {
                e.printStackTrace()
                it.resumeWithException(e)
            }
        }
    }

    fun downloadFirm(id: Int): ByteArray {
        val url = "${firmUrl}?id=$id"
        return get(url)
    }

    suspend fun download(id: Int, name: String) = suspendCoroutine<String> {

        val cb = { prog: Long ->
            bus.post(DownloadProgEvent(prog))
        }

        Thread {

            var fos: FileOutputStream? = null

            try {
                val dir = Environment.getExternalStorageDirectory()
                val file = File(dir, name)
                file.createNewFile()
                fos = FileOutputStream(file)
                download(id, fos, cb)
                it.resume(file.path)
            } catch (e: Exception) {
                it.resumeWithException(e)
                e.printStackTrace()
            } finally {
                close(fos)
            }

        }.start()
    }

    private fun download(id: Int, fos: FileOutputStream, cb: (prog: Long) -> Unit): Long {

        val c = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
        val req = Request.Builder()
            .url("${firmUrl}?id=$id")
            .get()
            .build()

        val res = c.newCall(req).execute()
        val body = res.body
        if (body == null) {
            throw IOException("net fail")
        }

        val length = body.contentLength()
        var total = 0L
        val input = body.byteStream()
        val buff = ByteArray(1024)
        defer({
            while (true) {
                val n = input.read(buff)
                if (n <= 0) {
                    break
                }
                fos.write(buff, 0, n)
                total += n
                cb(total * 100L / length)
            }
        }, {
            close(input)
        })
        return total
    }

    suspend fun getIpcFirm() = getFirm(FileTypeIpc)

    suspend fun getFirm(type: String) = suspendCoroutine<Array<FirmModel>> {

        NetTask.post {
            try {
                val body = get("$baseURL/firm/get?type=$type")
                val res = body.parseRespBody<Array<FirmModel>>()
                it.resume(res)
            } catch (e: Exception) {
                e.printStackTrace()
                it.resumeWithException(e)
            }
        }
    }
}

