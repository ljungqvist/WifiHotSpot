package com.fitc.wifihotspot.localeplugin

import android.content.Context
import info.ljungqvist.yaol.Observable
import info.ljungqvist.yaol.android.preferences.ObservablePreferenceFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Logger(context: Context) {

    private val logObservableInternal = ObservablePreferenceFactory.create(context, "LOGGER").stringPreference("LOG") { "==== LOG STARTS HERE ====\n" }
    val logObservable: Observable<String> = logObservableInternal
    var log by logObservableInternal
        private set

    fun log(message: String) = synchronized(logObservableInternal) {
        log = log.trim() + "${formatter.format(Date())} - $message\n"
    }

    fun clear() = synchronized(logObservableInternal) {
        log = "==== LOG STARTS HERE ====\n"
    }

}

private val formatter: DateFormat
    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
