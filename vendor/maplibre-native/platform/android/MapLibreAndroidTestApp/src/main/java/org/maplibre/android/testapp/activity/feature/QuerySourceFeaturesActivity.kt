package com.mapvina.android.testapp.activity.feature

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.mapvina.geojson.Feature
import com.mapvina.geojson.FeatureCollection
import com.mapvina.geojson.Point
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.style.expressions.Expression
import com.mapvina.android.style.layers.CircleLayer
import com.mapvina.android.style.sources.GeoJsonSource
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

/**
 * Test activity showcasing using the query source features API to query feature counts
 */
class QuerySourceFeaturesActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    private lateinit var mapvinaMap: MapVinaMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_source_features)

        // Initialize map as normal
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map: MapVinaMap? ->
            if (map != null) {
                mapvinaMap = map
            }
            mapvinaMap.getStyle { style: Style -> initStyle(style) }
            mapvinaMap.setStyle(TestStyles.getPredefinedStyleWithFallback("Streets"))
        }
    }

    private fun initStyle(style: Style) {
        // # --8<-- [start:JsonObject]
        val properties = JsonObject()
        properties.addProperty("key1", "value1")
        val source = GeoJsonSource(
            "test-source",
            FeatureCollection.fromFeatures(
                arrayOf(
                    Feature.fromGeometry(Point.fromLngLat(17.1, 51.0), properties),
                    Feature.fromGeometry(Point.fromLngLat(17.2, 51.0), properties),
                    Feature.fromGeometry(Point.fromLngLat(17.3, 51.0), properties),
                    Feature.fromGeometry(Point.fromLngLat(17.4, 51.0), properties)
                )
            )
        )
        style.addSource(source)
        val visible = Expression.eq(Expression.get("key1"), Expression.literal("value1"))
        val invisible = Expression.neq(Expression.get("key1"), Expression.literal("value1"))
        val layer = CircleLayer("test-layer", source.id)
            .withFilter(visible)
        style.addLayer(layer)
        // # --8<-- [end:JsonObject]

        // Add a click listener
        mapvinaMap.addOnMapClickListener { point: LatLng? ->
            // Query
            val features = source.querySourceFeatures(
                Expression.eq(
                    Expression.get("key1"),
                    Expression.literal("value1")
                )
            )
            Toast.makeText(
                this@QuerySourceFeaturesActivity,
                String.format(
                    "Found %s features",
                    features.size
                ),
                Toast.LENGTH_SHORT
            ).show()
            false
        }
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setColorFilter(ContextCompat.getColor(this, R.color.primary))
        fab.setOnClickListener { view: View? ->
            val visibility = layer.filter
            if (visibility != null && visibility == visible) {
                layer.setFilter(invisible)
                fab.setImageResource(R.drawable.ic_layers_clear)
            } else {
                layer.setFilter(visible)
                fab.setImageResource(R.drawable.ic_layers)
            }
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
