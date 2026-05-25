package com.mapvina.android.testapp.activity.camera

import android.graphics.Point
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.camera.CameraUpdateFactory
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

/**
 * Test activity showcasing the zoom Camera API.
 *
 * This includes zoomIn, zoomOut, zoomTo, zoomBy (center and custom focal point).
 */
class ManualZoomActivity : AppCompatActivity() {
    private lateinit var mapvinaMap: MapVinaMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_zoom)
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapvinaMap: MapVinaMap ->
            this@ManualZoomActivity.mapvinaMap = mapvinaMap
            mapvinaMap.setStyle(
                Style.Builder().fromUri(TestStyles.getPredefinedStyleWithFallback("Satellite Hybrid"))
            )
            val uiSettings = this@ManualZoomActivity.mapvinaMap.uiSettings
            uiSettings.setAllGesturesEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_zoom, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_zoom_in -> {
                // # --8<-- [start:zoomIn]
                mapvinaMap.animateCamera(CameraUpdateFactory.zoomIn())
                // # --8<-- [end:zoomIn]
                true
            }
            R.id.action_zoom_out -> {
                // # --8<-- [start:zoomOut]
                mapvinaMap.animateCamera(CameraUpdateFactory.zoomOut())
                // # --8<-- [end:zoomOut]
                true
            }
            R.id.action_zoom_by -> {
                // # --8<-- [start:zoomBy]
                mapvinaMap.animateCamera(CameraUpdateFactory.zoomBy(2.0))
                // # --8<-- [end:zoomBy]
                true
            }
            R.id.action_zoom_to -> {
                // # --8<-- [start:zoomTo]
                mapvinaMap.animateCamera(CameraUpdateFactory.zoomTo(2.0))
                // # --8<-- [end:zoomTo]
                true
            }
            R.id.action_zoom_to_point -> {
                // # --8<-- [start:zoomToPoint]
                val view = window.decorView
                mapvinaMap.animateCamera(
                    CameraUpdateFactory.zoomBy(
                        1.0,
                        Point(view.measuredWidth / 4, view.measuredHeight / 4)
                    )
                )
                // # --8<-- [end:zoomToPoint]
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
