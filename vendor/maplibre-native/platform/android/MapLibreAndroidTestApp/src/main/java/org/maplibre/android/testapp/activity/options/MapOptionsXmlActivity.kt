package com.mapvina.android.testapp.activity.options

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.OnMapReadyCallback
import com.mapvina.android.maps.Style
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

/**
 *  TestActivity demonstrating configuring MapView with XML
 */

class MapOptionsXmlActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var mapvinaMap: MapVinaMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_options_xml)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
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
