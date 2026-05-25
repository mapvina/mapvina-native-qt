package com.mapvina.android.testapp.maps

import android.view.SurfaceView
import android.view.TextureView
import android.view.ViewGroup
import androidx.test.annotation.UiThreadTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.mapvina.android.AppCenter
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMapOptions
import com.mapvina.android.testapp.activity.FeatureOverviewActivity
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4ClassRunner::class)
class RenderViewGetterTest : AppCenter() {

    @Rule
    @JvmField
    var rule = ActivityTestRule(FeatureOverviewActivity::class.java)

    private lateinit var rootView: ViewGroup
    private lateinit var mapView: MapView
    private val latch: CountDownLatch = CountDownLatch(1)

    @Test
    @UiThreadTest
    fun testSurfaceView() {
        rootView = rule.activity.findViewById(android.R.id.content)
        mapView = MapView(rule.activity)
        assertNotNull(mapView.renderView)
        assertTrue(mapView.renderView is SurfaceView)
    }

    @Test
    @UiThreadTest
    fun testTextureView() {
        rootView = rule.activity.findViewById(android.R.id.content)
        mapView = MapView(
            rule.activity,
            MapVinaMapOptions.createFromAttributes(rule.activity, null)
                .textureMode(true)
        )
        assertNotNull(mapView.renderView)
        assertTrue(mapView.renderView is TextureView)
    }
}
