package com.mapvina.android.location

import android.animation.TypeEvaluator
import androidx.annotation.Size
import com.mapvina.android.maps.MapVinaMap.CancelableCallback

class MapVinaPaddingAnimator internal constructor(
    @Size(min = 2) values: Array<DoubleArray>,
    updateListener: AnimationsValueChangeListener<DoubleArray>,
    cancelableCallback: CancelableCallback?
) :
    MapVinaAnimator<DoubleArray>(values, updateListener, Int.MAX_VALUE) {
    init {
        addListener(MapVinaAnimatorListener(cancelableCallback))
    }

    public override fun provideEvaluator(): TypeEvaluator<DoubleArray> {
        return PaddingEvaluator()
    }
}
