package com.mapvina.android.maps

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.mapvina.android.camera.CameraPosition
import com.mapvina.android.camera.CameraUpdateFactory
import com.mapvina.android.constants.MapVinaConstants
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.GesturesUiTestUtils.move
import com.mapvina.android.maps.GesturesUiTestUtils.quickScale
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.activity.BaseTest
import com.mapvina.android.testapp.activity.maplayout.SimpleMapActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MapGestureDetectorTest : BaseTest() {
    override fun getActivityClass() = SimpleMapActivity::class.java

    private var maxWidth: Int = 0
    private var maxHeight: Int = 0

    @Before
    fun setup() {
        maxWidth = mapView.width
        maxHeight = mapView.height
    }

    @Test
    fun sanity_quickZoom() {
        validateTestSetup()
        var initialZoom: Double? = null
        rule.runOnUiThread {
            initialZoom = mapvinaMap.cameraPosition.zoom
        }
        onView(withId(R.id.mapView)).perform(quickScale(maxHeight / 2f, withVelocity = false))
        rule.runOnUiThread {
            Assert.assertTrue(mapvinaMap.cameraPosition.zoom > initialZoom!!)
        }
    }

    @Test
    fun quickZoomDisabled_phantomQuickZoom_moveStillEnabled_15091() {
        // regression test for https://github.com/mapbox/mapbox-gl-native/issues/15091
        validateTestSetup()
        var initialCameraPosition: CameraPosition? = null
        rule.runOnUiThread {
            // zoom in so we can move vertically
            mapvinaMap.moveCamera(CameraUpdateFactory.zoomTo(4.0))
            initialCameraPosition = mapvinaMap.cameraPosition
            mapvinaMap.uiSettings.isQuickZoomGesturesEnabled = false
        }

        onView(withId(R.id.mapView)).perform(quickScale(maxHeight / 2f))
        rule.runOnUiThread {
            // camera did not move
            Assert.assertEquals(initialCameraPosition!!, mapvinaMap.cameraPosition)
        }

        // move to expected target
        onView(withId(R.id.mapView)).perform(move(-maxWidth / 2f, -maxHeight / 2f, withVelocity = false))
        rule.runOnUiThread {
            Assert.assertNotEquals(initialCameraPosition!!.target!!.latitude, mapvinaMap.cameraPosition.target!!.latitude, 1.0)
            Assert.assertNotEquals(initialCameraPosition!!.target!!.longitude, mapvinaMap.cameraPosition.target!!.longitude, 1.0)
        }
    }

    @Test
    fun quickZoom_doNotMove_14227() {
        // test for https://github.com/mapbox/mapbox-gl-native/issues/14227
        validateTestSetup()
        var initialTarget: LatLng? = null
        rule.runOnUiThread {
            initialTarget = mapvinaMap.cameraPosition.target!!
        }

        onView(withId(R.id.mapView)).perform(quickScale(maxHeight / 2f))
        rule.runOnUiThread {
            // camera did not move
            Assert.assertEquals(initialTarget!!.latitude, mapvinaMap.cameraPosition.target!!.latitude, 1.0)
            Assert.assertEquals(initialTarget!!.longitude, mapvinaMap.cameraPosition.target!!.longitude, 1.0)
        }
    }

    @Test
    fun quickZoom_interrupted_moveStillEnabled_14598() {
        // test for https://github.com/mapbox/mapbox-gl-native/issues/14598
        validateTestSetup()
        onView(withId(R.id.mapView)).perform(quickScale(maxHeight / 2f, interrupt = true))

        var initialCameraPosition: CameraPosition? = null
        rule.runOnUiThread {
            // zoom in so we can move vertically
            mapvinaMap.moveCamera(CameraUpdateFactory.zoomTo(4.0))
            initialCameraPosition = mapvinaMap.cameraPosition
            mapvinaMap.uiSettings.isQuickZoomGesturesEnabled = false
        }

        // move to expected target
        onView(withId(R.id.mapView)).perform(move(-maxWidth / 2f, -maxHeight / 2f, withVelocity = false))
        rule.runOnUiThread {
            Assert.assertNotEquals(initialCameraPosition!!.target!!.latitude, mapvinaMap.cameraPosition.target!!.latitude, 1.0)
            Assert.assertNotEquals(initialCameraPosition!!.target!!.longitude, mapvinaMap.cameraPosition.target!!.longitude, 1.0)
        }
    }

    @Test
    fun quickZoom_ignoreDoubleTap() {
        // test for https://github.com/mapbox/mapbox-gl-native/issues/14013
        validateTestSetup()
        var initialZoom: Double? = null
        rule.runOnUiThread {
            mapvinaMap.moveCamera(CameraUpdateFactory.zoomTo(2.0))
            initialZoom = mapvinaMap.cameraPosition.zoom
        }
        onView(withId(R.id.mapView)).perform(quickScale(-(mapvinaMap.gesturesManager.standardScaleGestureDetector.spanSinceStartThreshold * 2), withVelocity = false, duration = 1000L))
        R.id.mapView.loopFor(MapVinaConstants.ANIMATION_DURATION.toLong())
        rule.runOnUiThread {
            Assert.assertTrue(mapvinaMap.cameraPosition.zoom < initialZoom!!)
        }
    }

    @Test
    fun doubleTap_minimalMovement() {
        validateTestSetup()
        var initialZoom: Double? = null
        rule.runOnUiThread {
            initialZoom = mapvinaMap.cameraPosition.zoom
        }
        onView(withId(R.id.mapView)).perform(quickScale(mapvinaMap.gesturesManager.standardScaleGestureDetector.spanSinceStartThreshold / 2, withVelocity = false, duration = 50L))
        R.id.mapView.loopFor(MapVinaConstants.ANIMATION_DURATION.toLong())
        rule.runOnUiThread {
            Assert.assertEquals(initialZoom!! + 1, mapvinaMap.cameraPosition.zoom, 0.1)
        }
    }

    @Test
    fun doubleTap_overMaxThreshold_ignore_14013() {
        // test for https://github.com/mapbox/mapbox-gl-native/issues/14013
        validateTestSetup()
        var initialZoom: Double? = null
        rule.runOnUiThread {
            initialZoom = mapvinaMap.cameraPosition.zoom
            mapvinaMap.uiSettings.isQuickZoomGesturesEnabled = false
        }
        onView(withId(R.id.mapView)).perform(quickScale(mapvinaMap.gesturesManager.standardScaleGestureDetector.spanSinceStartThreshold * 2, withVelocity = false, duration = 50L))
        R.id.mapView.loopFor(MapVinaConstants.ANIMATION_DURATION.toLong())
        rule.runOnUiThread {
            Assert.assertEquals(initialZoom!!, mapvinaMap.cameraPosition.zoom, 0.01)
        }
    }

    @Test
    fun doubleTap_interrupted_moveStillEnabled() {
        validateTestSetup()

        rule.runOnUiThread {
            mapvinaMap.moveCamera(CameraUpdateFactory.zoomTo(4.0))
        }

        onView(withId(R.id.mapView)).perform(quickScale(mapvinaMap.gesturesManager.standardScaleGestureDetector.spanSinceStartThreshold / 2, withVelocity = false, duration = 50L, interrupt = true))

        var initialCameraPosition: CameraPosition? = null
        rule.runOnUiThread {
            // zoom in so we can move vertically
            mapvinaMap.moveCamera(CameraUpdateFactory.zoomTo(4.0))
            initialCameraPosition = mapvinaMap.cameraPosition
            mapvinaMap.uiSettings.isQuickZoomGesturesEnabled = false
        }

        // move to expected target
        onView(withId(R.id.mapView)).perform(move(-maxWidth / 2f, -maxHeight / 2f, withVelocity = false))
        rule.runOnUiThread {
            Assert.assertNotEquals(initialCameraPosition!!.target!!.latitude, mapvinaMap.cameraPosition.target!!.latitude, 1.0)
            Assert.assertNotEquals(initialCameraPosition!!.target!!.longitude, mapvinaMap.cameraPosition.target!!.longitude, 1.0)
        }
    }

    @Test
    fun quickZoom_roundTripping() {
        validateTestSetup()
        rule.runOnUiThread {
            mapvinaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.0, 16.0), 3.0))
        }
        onView(withId(R.id.mapView)).perform(quickScale(300f, withVelocity = false, duration = 750L))
        onView(withId(R.id.mapView)).perform(quickScale(-300f, withVelocity = false, duration = 750L))

        rule.runOnUiThread {
            Assert.assertEquals(3.0, mapvinaMap.cameraPosition.zoom, 0.01)
        }
    }
}
