package com.fitc.wifihotspot.localeplugin

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver

class HotSpotBroadcastReceiver : AbstractPluginSettingReceiver() {

    override fun isBundleValid(bundle: Bundle): Boolean = bundle.isValid()

    override fun isAsync(): Boolean = false

    override fun firePluginSetting(context: Context, bundle: Bundle) {

        Log.i("HotSpotBroadcastReceiver", "Turn HotSpot ${if (bundle.hotspotOn) "ON" else "OFF"}")
        WorkManager.getInstance(context).enqueue(
                OneTimeWorkRequest.Builder(HotSpotWorker::class.java)
                        .setInputData(HotSpotWorker.inputData(bundle.hotspotOn))
                        .build()
        )

    }

}