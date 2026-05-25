package com.mapvina.android.testapp.activity

import androidx.annotation.UiThread
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.testapp.activity.espresso.EspressoTestActivity
import com.mapvina.android.testapp.styles.TestStyles

/**
 * Base class for all tests using EspressoTestActivity as wrapper.
 *
 *
 * Loads "assets/streets.json" as style.
 *
 */
open class EspressoTest : BaseTest() {
    override fun getActivityClass(): Class<*> {
        return EspressoTestActivity::class.java
    }

    @UiThread
    override fun initMap(mapvinaMap: MapVinaMap) {
        mapvinaMap.setStyle(Style.Builder().fromUri(TestStyles.OPENFREEMAP_LIBERTY))
        super.initMap(mapvinaMap)
    }
}
