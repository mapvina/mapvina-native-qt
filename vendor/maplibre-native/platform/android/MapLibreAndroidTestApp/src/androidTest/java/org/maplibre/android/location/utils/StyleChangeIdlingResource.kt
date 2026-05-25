package com.mapvina.android.location.utils

import androidx.test.espresso.IdlingResource
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style

/**
 * Resource, that's idling until the provided style is loaded.
 * Remember to add any espresso action (like view assertion) after the [waitForStyle] call
 * for the test to keep running.
 */
class StyleChangeIdlingResource : IdlingResource {

    private var callback: IdlingResource.ResourceCallback? = null
    private var isIdle = true

    override fun getName(): String {
        return javaClass.simpleName
    }

    override fun isIdleNow(): Boolean {
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    private fun setIdle() {
        isIdle = true
        callback?.onTransitionToIdle()
    }

    fun waitForStyle(mapvinaMap: MapVinaMap, styleUrl: String) {
        isIdle = false
        mapvinaMap.setStyle(Style.Builder().fromUri(styleUrl)) {
            setIdle()
        }
    }
}
