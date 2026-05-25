package com.mapvina.android.testapp.activity.style

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.maps.*
import com.mapvina.android.style.layers.HillshadeLayer
import com.mapvina.android.style.sources.RasterDemSource
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

/**
 * Test activity showcasing using HillshadeLayer.
 */
class HillshadeLayerActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var mapvinaMap: MapVinaMap
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_extrusion_layer)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { map: MapVinaMap? ->
                if (map != null) {
                    mapvinaMap = map
                }
                val rasterDemSource = RasterDemSource(SOURCE_ID, SOURCE_URL)
                val hillshadeLayer = HillshadeLayer(LAYER_ID, SOURCE_ID)
                mapvinaMap.setStyle(
                    Style.Builder()
                        .fromUri(TestStyles.getPredefinedStyleWithFallback("Streets"))
                        .withLayerBelow(hillshadeLayer, LAYER_BELOW_ID)
                        .withSource(rasterDemSource)
                )
            }
        )
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

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    public override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    companion object {
        private const val LAYER_ID = "hillshade"
        private const val LAYER_BELOW_ID = "water_intermittent"
        private const val SOURCE_ID = "terrain-rgb"
        private const val SOURCE_URL = "maptiler://sources/terrain-rgb"
    }
}
