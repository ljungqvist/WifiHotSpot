package com.fitc.wifihotspot.localeplugin

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fitc.wifihotspot.MyOnStartTetheringCallback
import com.fitc.wifihotspot.MyOreoWifiManager
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HotSpotWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val logger = Logger(context)

    private val manager = MyOreoWifiManager(context)

    override fun doWork(): Result =
            if (inputData.getBoolean(TURN_ON_KEY, false)) {
                logger.log("W ON")

                var result: Result = Result.retry()

                val latch = CountDownLatch(1)

                val callback = object : MyOnStartTetheringCallback() {
                    override fun onTetheringStarted() {
                        result = Result.success()
                        logger.log("W ON SUCCESS")
                        latch.countDown()
                    }

                    override fun onTetheringFailed() {
                        result = Result.failure()
                        logger.log("W ON FAILED")
                        latch.countDown()
                    }
                }

                manager.startTethering(callback)
                latch.await(5L, TimeUnit.SECONDS)
                logger.log("W ON DONE")
                result
            } else {
                logger.log("W OFF")
                manager.stopTethering()
                logger.log("W OFF DONE")
                Result.success()
            }


    companion object {
        fun inputData(turnOn: Boolean): Data = Data.Builder()
                .putBoolean(TURN_ON_KEY, turnOn)
                .build()
    }

}

private const val TURN_ON_KEY = "com.fitc.wifihotspot.localeplugin.HotSpotWorker.TURN_ON"
