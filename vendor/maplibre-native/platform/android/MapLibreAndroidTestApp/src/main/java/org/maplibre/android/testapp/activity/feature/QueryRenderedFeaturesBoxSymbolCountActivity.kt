package com.mapvina.android.testapp.activity.feature

import android.graphics.BitmapFactory
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.style.expressions.Expression
import com.mapvina.android.style.layers.BackgroundLayer
import com.mapvina.android.style.layers.PropertyFactory
import com.mapvina.android.style.layers.SymbolLayer
import com.mapvina.android.style.sources.GeoJsonSource
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.utils.ResourceUtils
import timber.log.Timber
import java.io.IOException

/**
 * Test activity showcasing using the query rendered features API to count Symbols in a rectangle.
 */
class QueryRenderedFeaturesBoxSymbolCountActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    lateinit var mapvinaMap: MapVinaMap
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_features_box)
        val selectionBox = findViewById<View>(R.id.selection_box)

        // Initialize map as normal
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapvinaMap: MapVinaMap ->
            this@QueryRenderedFeaturesBoxSymbolCountActivity.mapvinaMap = mapvinaMap
            try {
                val testPoints = ResourceUtils.readRawResource(
                    mapView.context,
                    R.raw.test_points_utrecht
                )
                val markerImage =
                    BitmapFactory.decodeResource(resources, R.drawable.mapvina_marker_icon_default)
                mapvinaMap.setStyle(
                    Style.Builder()
                        .withLayer(
                            BackgroundLayer("bg")
                                .withProperties(
                                    PropertyFactory.backgroundColor(Expression.rgb(120, 161, 226))
                                )
                        )
                        .withLayer(
                            SymbolLayer("symbols-layer", "symbols-source")
                                .withProperties(
                                    PropertyFactory.iconImage("test-icon")
                                )
                        )
                        .withSource(
                            GeoJsonSource("symbols-source", testPoints)
                        )
                        .withImage("test-icon", markerImage)
                )
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
            selectionBox.setOnClickListener { view: View? ->
                // Query
                val top = selectionBox.top - mapView.top
                val left = selectionBox.left - mapView.left
                val box = RectF(
                    left.toFloat(),
                    top.toFloat(),
                    (left + selectionBox.width).toFloat(),
                    (top + selectionBox.height).toFloat()
                )
                Timber.i("Querying box %s", box)
                val features = mapvinaMap.queryRenderedFeatures(box, "symbols-layer")

                // Show count
                 Toast.makeText(
                    this@QueryRenderedFeaturesBoxSymbolCountActivity,
                    "${features.size} feature${if (features.size == 1) "" else "s"} in box",
                    Toast.LENGTH_SHORT
                ).show()
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
