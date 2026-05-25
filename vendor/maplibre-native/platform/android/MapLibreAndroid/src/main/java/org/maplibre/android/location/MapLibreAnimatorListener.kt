package com.mapvina.android.location

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import com.mapvina.android.maps.MapVinaMap

internal class MapVinaAnimatorListener(cancelableCallback: MapVinaMap.CancelableCallback?) :
    AnimatorListenerAdapter() {
    private val cancelableCallback: MapVinaMap.CancelableCallback?

    init {
        this.cancelableCallback = cancelableCallback
    }

    override fun onAnimationCancel(animation: Animator) {
        cancelableCallback?.onCancel()
    }

    override fun onAnimationEnd(animation: Animator) {
        cancelableCallback?.onFinish()
    }
}
