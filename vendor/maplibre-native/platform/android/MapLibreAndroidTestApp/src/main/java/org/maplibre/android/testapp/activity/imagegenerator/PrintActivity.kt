package com.mapvina.android.testapp.activity.imagegenerator

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.print.PrintHelper
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

/**
 * Test activity showcasing using the Snapshot API to print a Map.
 */
class PrintActivity : AppCompatActivity(), MapVinaMap.SnapshotReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var mapvinaMap: MapVinaMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this::initMap)
        val fab = findViewById<View>(R.id.fab)
        fab?.setOnClickListener { _: View? ->
            if (this::mapvinaMap.isInitialized && mapvinaMap.style != null) {
                mapvinaMap.snapshot(this@PrintActivity)
            }
        }
    }

    private fun initMap(mapvinaMap: MapVinaMap) {
        this.mapvinaMap = mapvinaMap
        mapvinaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets"))
    }

    override fun onSnapshotReady(snapshot: Bitmap) {
        val photoPrinter = PrintHelper(this)
        photoPrinter.scaleMode = PrintHelper.SCALE_MODE_FIT
        photoPrinter.printBitmap("map.jpg - mapbox print job", snapshot)
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
