package com.mapvina.android.testapp.maps

import androidx.test.espresso.UiController
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.style.layers.SymbolLayer
import com.mapvina.android.style.sources.GeoJsonSource
import com.mapvina.android.testapp.action.MapVinaMapAction
import com.mapvina.android.testapp.activity.EspressoTest
import com.mapvina.android.testapp.utils.TestingAsyncUtils
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class StyleLoadTest : EspressoTest() {

    @Test
    fun updateSourceAfterStyleLoad() {
        validateTestSetup()
        MapVinaMapAction.invoke(mapvinaMap) { uiController: UiController, mapvinaMap: MapVinaMap ->
            val source = GeoJsonSource("id")
            val layer = SymbolLayer("id", "id")
            mapvinaMap.setStyle(Style.Builder().withSource(source).withLayer(layer))
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            mapvinaMap.setStyle(Style.getPredefinedStyles()[0].url)
            TestingAsyncUtils.waitForLayer(uiController, mapView)
            source.setGeoJson("{}")
        }
    }
}
