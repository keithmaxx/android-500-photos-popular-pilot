package com.fgsveto.a500pxphotospilot

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_FILENAME = "com.fgsveto.a500pxphotospilot.prefs"
private const val DETAILS_HINT_SHOWN = "details_hint"

class Prefs (context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var isHintShown: Boolean
        get() = prefs.getBoolean(DETAILS_HINT_SHOWN, false)
        set(value) = prefs.edit().putBoolean(DETAILS_HINT_SHOWN, value).apply()
}