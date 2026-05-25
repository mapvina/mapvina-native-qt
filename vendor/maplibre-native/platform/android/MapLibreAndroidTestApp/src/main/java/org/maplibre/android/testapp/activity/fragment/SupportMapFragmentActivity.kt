package com.mapvina.android.testapp.activity.fragment

import android.os.Bundle // ktlint-disable import-ordering
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.camera.CameraPosition
import com.mapvina.android.camera.CameraUpdateFactory
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.* // ktlint-disable no-wildcard-imports
import com.mapvina.android.maps.MapFragment.OnMapViewReadyCallback
import com.mapvina.android.maps.MapView.OnDidFinishRenderingFrameListener
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

/**
 * Test activity showcasing using the MapFragment API using Support Library Fragments.
 *
 *
 * Uses MapVinaMapOptions to initialise the Fragment.
 *
 */
class SupportMapFragmentActivity :
    AppCompatActivity(),
    OnMapViewReadyCallback,
    OnMapReadyCallback,
    OnDidFinishRenderingFrameListener {
    private lateinit var mapvinaMap: MapVinaMap
    private lateinit var mapView: MapView
    private var initialCameraAnimation = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_fragment)
        val mapFragment: SupportMapFragment?
        if (savedInstanceState == null) {
            mapFragment = SupportMapFragment.newInstance(createFragmentOptions())
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, mapFragment, TAG)
                .commit()
        } else {
            mapFragment = supportFragmentManager.findFragmentByTag(TAG) as SupportMapFragment?
        }
        mapFragment!!.getMapAsync(this)
    }

    private fun createFragmentOptions(): MapVinaMapOptions {
        val options = MapVinaMapOptions.createFromAttributes(this, null)
        options.scrollGesturesEnabled(false)
        options.zoomGesturesEnabled(false)
        options.tiltGesturesEnabled(false)
        options.rotateGesturesEnabled(false)
        options.debugActive(false)
        val dc = LatLng(38.90252, -77.02291)
        options.minZoomPreference(9.0)
        options.maxZoomPreference(11.0)
        options.camera(
            CameraPosition.Builder()
                .target(dc)
                .zoom(11.0)
                .build()
        )
        return options
    }

    override fun onMapViewReady(map: MapView) {
        mapView = map
        mapView.addOnDidFinishRenderingFrameListener(this)
    }

    override fun onMapReady(map: MapVinaMap) {
        mapvinaMap = map
        mapvinaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Satellite Hybrid"))
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.removeOnDidFinishRenderingFrameListener(this)
    }

    override fun onDidFinishRenderingFrame(fully: Boolean, frameEncodingTime: Double, frameRenderingTime: Double) {
        if (initialCameraAnimation && fully && this::mapvinaMap.isInitialized) {
            mapvinaMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().tilt(45.0).build()),
                5000
            )
            initialCameraAnimation = false
        }
    }

    companion object {
        private const val TAG = "com.mapbox.map"
    }
}
