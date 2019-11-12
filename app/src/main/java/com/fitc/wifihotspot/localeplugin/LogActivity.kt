package com.fitc.wifihotspot.localeplugin

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.fitc.wifihotspot.databinding.ActivityLogBinding
import info.ljungqvist.yaol.android.observableField

class LogActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val logger = Logger(this)

        val binding = ActivityLogBinding.inflate(layoutInflater)
        binding.clear = View.OnClickListener {
            logger.clear()
        }
        binding.log = logger.logObservable.observableField()

        setContentView(binding.root)

    }

}


