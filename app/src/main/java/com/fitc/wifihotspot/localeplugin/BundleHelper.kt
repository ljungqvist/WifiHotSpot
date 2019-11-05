package com.fitc.wifihotspot.localeplugin

import android.os.Bundle

fun Bundle.isValid(): Boolean =
    containsKey(HOTSPOT_ON_KEY)

var Bundle.hotspotOn: Boolean
    get() = getBoolean(HOTSPOT_ON_KEY)
    set(value) = putBoolean(HOTSPOT_ON_KEY, value)


private const val HOTSPOT_ON_KEY = "com.fitc.wifihotspot.localeplugin.HOTSPOT_ON"
