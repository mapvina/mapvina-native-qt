package com.mapvina.android.testapp.activity.customlayer

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapvina.android.camera.CameraUpdateFactory
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.style.layers.CustomLayer
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.model.customlayer.ExampleCustomLayer
import com.mapvina.android.testapp.styles.TestStyles

/**
 * Test activity showcasing the Custom Layer API
 *
 *
 * Note: experimental API, do not use.
 *
 */
class CustomLayerActivity : AppCompatActivity() {
    private lateinit var mapvinaMap: MapVinaMap
    private lateinit var mapView: MapView
    private var customLayer: CustomLayer? = null
    private lateinit var fab: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_layer)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map: MapVinaMap ->
            mapvinaMap = map
            mapvinaMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(39.91448, -243.60947),
                    10.0
                )
            )
            mapvinaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets")) { _: Style? -> initFab() }
        }
    }

    private fun initFab() {
        fab = findViewById(R.id.fab)
        fab.setColorFilter(ContextCompat.getColor(this, R.color.primary))
        fab.setOnClickListener { _: View? ->
            if (this::mapvinaMap.isInitialized) {
                swapCustomLayer()
            }
        }
    }

    private fun swapCustomLayer() {
        val style = mapvinaMap.style
        if (customLayer != null) {
            style!!.removeLayer(customLayer!!)
            customLayer = null
            fab.setImageResource(R.drawable.ic_layers)
        } else {
            customLayer = CustomLayer(
                "custom",
                ExampleCustomLayer.createContext()
            )
            style!!.addLayerBelow(customLayer!!, "building")
            fab.setImageResource(R.drawable.ic_layers_clear)
        }
    }

    private fun updateLayer() {
        if (this::mapvinaMap.isInitialized) {
            mapvinaMap.triggerRepaint()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_custom_layer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update_layer -> {
                updateLayer()
                true
            }
            R.id.action_set_color_red -> {
                ExampleCustomLayer.setColor(1f, 0f, 0f, 1f)
                true
            }
            R.id.action_set_color_green -> {
                ExampleCustomLayer.setColor(0f, 1f, 0f, 1f)
                true
            }
            R.id.action_set_color_blue -> {
                ExampleCustomLayer.setColor(0f, 0f, 1f, 1f)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
