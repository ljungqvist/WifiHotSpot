package com.fitc.wifihotspot.localeplugin

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fitc.wifihotspot.MyOnStartTetheringCallback
import com.fitc.wifihotspot.MyOreoWifiManager
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HotSpotWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val manager = MyOreoWifiManager(applicationContext)

        if (inputData.getBoolean(TURN_ON_KEY, false)) {

            val latch = CountDownLatch(1)

            val callback = object : MyOnStartTetheringCallback() {
                override fun onTetheringStarted() {
                    Log.i("HotSpotWorker", "onTetheringStarted")
                    latch.countDown()
                }

                override fun onTetheringFailed() {
                    Log.i("HotSpotWorker", "onTetheringFailed")
                    latch.countDown()
                }
            }

            latch.await(5L, TimeUnit.SECONDS)
            manager.startTethering(callback)
        } else {
            manager.stopTethering()
        }
        return Result.success()

    }

    companion object {
        fun inputData(turnOn: Boolean): Data = Data.Builder()
                .putBoolean(TURN_ON_KEY, turnOn)
                .build()
    }

}

private const val TURN_ON_KEY = "com.fitc.wifihotspot.localeplugin.HotSpotWorker.TURN_ON"
