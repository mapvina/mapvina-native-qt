package com.mapvina.android.testapp.activity.style

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.geojson.Point
import com.mapvina.android.camera.CameraPosition
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.style.expressions.Expression.distance
import com.mapvina.android.style.expressions.Expression.lt
import com.mapvina.android.style.layers.FillLayer
import com.mapvina.android.style.layers.Property.NONE
import com.mapvina.android.style.layers.PropertyFactory.*
import com.mapvina.android.style.layers.SymbolLayer
import com.mapvina.android.style.sources.GeoJsonSource
import com.mapvina.android.testapp.databinding.ActivityWithinExpressionBinding
import com.mapvina.android.testapp.styles.TestStyles
import com.mapvina.turf.TurfConstants
import com.mapvina.turf.TurfTransformation

/**
 * An Activity that showcases the within expression to filter features outside a geometry
 */
class DistanceExpressionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWithinExpressionBinding
    private lateinit var mapvinaMap: MapVinaMap

    private val lat = 37.78794572301525
    private val lon = -122.40752220153807

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithinExpressionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            mapvinaMap = map

            // Setup camera position above Georgetown
            mapvinaMap.cameraPosition = CameraPosition.Builder()
                .target(LatLng(lat, lon))
                .zoom(16.0)
                .build()
            setupStyle()
        }
    }

    private fun setupStyle() {
        // --8<-- [start:FillLayer]
        val center = Point.fromLngLat(lon, lat)
        val circle = TurfTransformation.circle(center, 150.0, TurfConstants.UNIT_METRES)
        mapvinaMap.setStyle(
            Style.Builder()
                .fromUri(TestStyles.OPENFREEMAP_BRIGHT)
                .withSources(
                    GeoJsonSource(
                        POINT_ID,
                        Point.fromLngLat(lon, lat)
                    ),
                    GeoJsonSource(CIRCLE_ID, circle)
                )
                .withLayerBelow(
                    FillLayer(CIRCLE_ID, CIRCLE_ID)
                        .withProperties(
                            fillOpacity(0.5f),
                            fillColor(Color.parseColor("#3bb2d0"))
                        ),
                    "poi"
                )
            // --8<-- [end:FillLayer]
        ) { style ->
            // --8<-- [start:distanceExpression]
            for (layer in style.layers) {
                if (layer is SymbolLayer) {
                    if (layer.id.startsWith("poi")) {
                        layer.setFilter(lt(
                            distance(
                                Point.fromLngLat(lon, lat)
                            ),
                            150
                        ))
                    } else {
                        layer.setProperties(visibility(NONE))
                    }
                }
            }
            // --8<-- [end:distanceExpression]
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.let {
            binding.mapView.onSaveInstanceState(it)
        }
    }

    companion object {
        const val POINT_ID = "point"
        const val CIRCLE_ID = "circle"
    }
}
