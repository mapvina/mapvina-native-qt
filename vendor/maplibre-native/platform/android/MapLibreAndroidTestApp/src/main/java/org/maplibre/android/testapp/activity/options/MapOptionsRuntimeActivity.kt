package com.mapvina.android.testapp.activity.options

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.camera.CameraPosition
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.MapVinaMapOptions
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.OnMapReadyCallback
import com.mapvina.android.maps.Style
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

/**
 *  TestActivity demonstrating configuring MapView with MapOptions
 */
class MapOptionsRuntimeActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapvinaMap: MapVinaMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_options_runtime)

        // Create map configuration
        val mapvinaMapOptions = MapVinaMapOptions.createFromAttributes(this)
        mapvinaMapOptions.apply {
            apiBaseUri("https://api.mapvina.com")
            camera(
                CameraPosition.Builder()
                    .bearing(0.0)
                    .target(LatLng(42.31230486601532, 64.63967338936439))
                    .zoom(3.9)
                    .tilt(0.0)
                    .build()
            )
            maxPitchPreference(90.0)
            minPitchPreference(0.0)
            maxZoomPreference(26.0)
            minZoomPreference(2.0)
            localIdeographFontFamily("Droid Sans")
            zoomGesturesEnabled(true)
            compassEnabled(true)
            compassFadesWhenFacingNorth(true)
            scrollGesturesEnabled(true)
            rotateGesturesEnabled(true)
            tiltGesturesEnabled(true)
        }

        // Create map programmatically, add to view hierarchy
        mapView = MapView(this, mapvinaMapOptions)
        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)
        (findViewById<View>(R.id.container) as ViewGroup).addView(mapView)
    }

    override fun onMapReady(mapvinaMap: MapVinaMap) {
        this.mapvinaMap = mapvinaMap
        this.mapvinaMap.setStyle("https://maps.mapvina.com/styles/v2/streets.json?key=public_key")
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
