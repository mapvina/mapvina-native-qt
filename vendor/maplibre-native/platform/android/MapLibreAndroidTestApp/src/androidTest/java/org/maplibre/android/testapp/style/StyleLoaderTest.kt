package com.mapvina.android.testapp.style

import android.view.View
import androidx.test.espresso.UiController
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.action.MapVinaMapAction
import com.mapvina.android.testapp.activity.EspressoTest
import com.mapvina.android.testapp.utils.ResourceUtils.readRawResource
import org.junit.Assert
import org.junit.Test
import java.io.IOException

/**
 * Tests around style loading
 */
class StyleLoaderTest : EspressoTest() {
    @Test
    fun testSetGetStyleJsonString() {
        validateTestSetup()
        MapVinaMapAction.invoke(
            mapvinaMap
        ) { uiController: UiController?, mapvinaMap: MapVinaMap ->
            try {
                val expected =
                    readRawResource(
                        rule.activity,
                        R.raw.local_style
                    )
                mapvinaMap.setStyle(Style.Builder().fromJson(expected))
                val actual = mapvinaMap.style!!.json
                Assert.assertEquals("Style json should match", expected, actual)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
    }

    @Test
    fun testDefaultStyleLoadWithActivityLifecycleChange() {
        validateTestSetup()
        MapVinaMapAction.invoke(
            mapvinaMap
        ) { uiController: UiController?, mapvinaMap: MapVinaMap ->
            try {
                val expected =
                    readRawResource(
                        rule.activity,
                        R.raw.local_style
                    )
                mapvinaMap.setStyle(Style.Builder().fromJson(expected))

                // fake activity stop/start
                val mapView =
                    rule.activity.findViewById<View>(R.id.mapView) as MapView
                mapView.onPause()
                mapView.onStop()
                mapView.onStart()
                mapView.onResume()
                val actual = mapvinaMap.style!!.json
                Assert.assertEquals(
                    "Style URL should be empty",
                    "",
                    mapvinaMap.style!!.uri
                )
                Assert.assertEquals("Style json should match", expected, actual)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
    }
}
