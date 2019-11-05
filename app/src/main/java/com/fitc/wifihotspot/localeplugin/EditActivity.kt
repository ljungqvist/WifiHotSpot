package com.fitc.wifihotspot.localeplugin

import android.os.Bundle
import android.view.View
import com.fitc.wifihotspot.databinding.ActivityEditBinding
import com.twofortyfouram.locale.sdk.client.ui.activity.AbstractFragmentPluginActivity
import info.ljungqvist.yaol.MutableObservable
import info.ljungqvist.yaol.android.primitive
import info.ljungqvist.yaol.mutableObservable

class EditActivity : AbstractFragmentPluginActivity() {

    private val hotspotOnObservable: MutableObservable<Boolean> = mutableObservable(false)
    private var hotspotOn by hotspotOnObservable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            hotspotOn = it.hotspotOn
        }

        val binding = ActivityEditBinding.inflate(layoutInflater)
        binding.hotspotOn = hotspotOnObservable.primitive()
        binding.onClick = View.OnClickListener {
            hotspotOn = hotspotOn.not()
        }

        setContentView(binding.root)


    }

    override fun isBundleValid(bundle: Bundle): Boolean = bundle.isValid()

    override fun onPostCreateWithPreviousResult(bundle: Bundle, blurb: String) {
        hotspotOn = bundle.hotspotOn
    }

    override fun getResultBundle(): Bundle? =
            Bundle().also {
                it.hotspotOn = hotspotOn
            }

    override fun getResultBlurb(p0: Bundle): String = when {
        hotspotOn -> "On"
        else -> "Off"
    }
}
