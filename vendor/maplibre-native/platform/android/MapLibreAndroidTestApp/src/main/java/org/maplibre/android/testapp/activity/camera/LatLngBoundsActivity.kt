package com.mapvina.android.testapp.activity.camera

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapvina.geojson.FeatureCollection
import com.mapvina.geojson.FeatureCollection.fromJson
import com.mapvina.geojson.Point
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.geometry.LatLngBounds
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.Style
import com.mapvina.android.style.layers.Property.ICON_ANCHOR_CENTER
import com.mapvina.android.style.layers.PropertyFactory.*
import com.mapvina.android.style.layers.SymbolLayer
import com.mapvina.android.style.sources.GeoJsonSource
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.databinding.ActivityLatlngboundsBinding
import com.mapvina.android.testapp.styles.TestStyles
import com.mapvina.android.testapp.utils.GeoParseUtil
import com.mapvina.android.utils.BitmapUtils
import java.net.URISyntaxException

/** Test activity showcasing using the LatLngBounds camera API. */
class LatLngBoundsActivity : AppCompatActivity() {

    private lateinit var mapvinaMap: MapVinaMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var bounds: LatLngBounds
    private lateinit var binding: ActivityLatlngboundsBinding

    private val peekHeight by lazy {
        375.toPx(this) // 375dp
    }

    private val additionalPadding by lazy {
        32.toPx(this) // 32dp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatlngboundsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initMapView(savedInstanceState)
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            mapvinaMap = map

            // # --8<-- [start:featureCollection]
            val featureCollection: FeatureCollection =
                fromJson(GeoParseUtil.loadStringFromAssets(this, "points-sf.geojson"))
            bounds = createBounds(featureCollection)

            map.getCameraForLatLngBounds(bounds, createPadding(peekHeight))?.let {
                map.cameraPosition = it
            }
            // # --8<-- [end:featureCollection]

            try {
                loadStyle(featureCollection)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
    }

    private fun loadStyle(featureCollection: FeatureCollection) {
        mapvinaMap.setStyle(
            Style.Builder()
                .fromUri(TestStyles.OPENFREEMAP_LIBERTY)
                .withLayer(
                    SymbolLayer("symbol", "symbol")
                        .withProperties(
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true),
                            iconImage("icon"),
                            iconAnchor(ICON_ANCHOR_CENTER)
                        )
                )
                .withSource(GeoJsonSource("symbol", featureCollection))
                .withImage(
                    "icon",
                    BitmapUtils.getDrawableFromRes(
                        this@LatLngBoundsActivity,
                        R.drawable.ic_android
                    )!!
                )
        ) {
            initBottomSheet()
            binding.fab.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val offset = convertSlideOffset(slideOffset)
                    val bottomPadding = (peekHeight * offset).toInt()

                    mapvinaMap.getCameraForLatLngBounds(bounds, createPadding(bottomPadding))
                        ?.let { mapvinaMap.cameraPosition = it }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    // no-op
                }
            }
        )
    }

    // slideOffset ranges from NaN to -1.0, range from 1.0 to 0 instead
    fun convertSlideOffset(slideOffset: Float): Float {
        return if (slideOffset.equals(Float.NaN)) {
            1.0f
        } else {
            1 + slideOffset
        }
    }

    fun createPadding(bottomPadding: Int): IntArray {
        return intArrayOf(additionalPadding, additionalPadding, additionalPadding, bottomPadding)
    }

    // # --8<-- [start:createBounds]
    private fun createBounds(featureCollection: FeatureCollection): LatLngBounds {
        val boundsBuilder = LatLngBounds.Builder()
        featureCollection.features()?.let {
            for (feature in it) {
                val point = feature.geometry() as Point
                boundsBuilder.include(LatLng(point.latitude(), point.longitude()))
            }
        }
        return boundsBuilder.build()
    }
    // # --8<-- [end:createBounds]

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}

fun Int.toPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
