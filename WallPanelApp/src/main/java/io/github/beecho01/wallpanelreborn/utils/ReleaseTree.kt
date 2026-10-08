package io.github.beecho01.wallpanelreborn.utils

import android.util.Log
import timber.log.Timber

/** Release logging: only warnings and errors reach logcat. */
class ReleaseTree : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean = priority >= Log.WARN

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Log.println(priority, tag ?: "WallPanelReborn", if (t != null) "$message\n${Log.getStackTraceString(t)}" else message)
    }
}
