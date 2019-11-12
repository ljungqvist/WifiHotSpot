package com.fitc.wifihotspot.localeplugin

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fitc.wifihotspot.MyOnStartTetheringCallback
import com.fitc.wifihotspot.MyOreoWifiManager
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HotSpotWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val logger = Logger(context)

    override fun doWork(): Result {
        logger.log("doWork")

        val manager = MyOreoWifiManager(applicationContext)

        if (inputData.getBoolean(TURN_ON_KEY, false)) {
            logger.log("doWork ON")

            val latch = CountDownLatch(1)

            val callback = object : MyOnStartTetheringCallback() {
                override fun onTetheringStarted() {
                    Log.i("HotSpotWorker", "onTetheringStarted")
                    logger.log("doWork ON SUCCESS")
                    latch.countDown()
                }

                override fun onTetheringFailed() {
                    Log.i("HotSpotWorker", "onTetheringFailed")
                    logger.log("doWork ON FAILED")
                    latch.countDown()
                }
            }

            manager.startTethering(callback)
            latch.await(5L, TimeUnit.SECONDS)
        } else {
            logger.log("doWork OFF")
            manager.stopTethering()
            logger.log("doWork OFF SUCCESS")
        }
        logger.log("doWork RETURN")
        return Result.success()

    }

    companion object {
        fun inputData(turnOn: Boolean): Data = Data.Builder()
                .putBoolean(TURN_ON_KEY, turnOn)
                .build()
    }

}

private const val TURN_ON_KEY = "com.fitc.wifihotspot.localeplugin.HotSpotWorker.TURN_ON"
