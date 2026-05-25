package com.mapvina.android.testapp.activity.camera

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.camera.CameraPosition
import com.mapvina.android.camera.CameraUpdateFactory
import com.mapvina.android.geometry.LatLng
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.MapVinaMap.CancelableCallback
import com.mapvina.android.maps.MapVinaMap.OnCameraIdleListener
import com.mapvina.android.maps.OnMapReadyCallback
import com.mapvina.android.maps.Style
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles
import timber.log.Timber

/**
 * Test activity showcasing the Camera API and listen to camera updates by animating the camera
 * above London.
 *
 * Shows how to use animate, ease and move camera update factory methods.
 */
class CameraAnimationTypeActivity : AppCompatActivity(), OnMapReadyCallback {
    // # --8<-- [start:callback]
    private val callback: CancelableCallback =
        object : CancelableCallback {
            override fun onCancel() {
                Timber.i("Duration onCancel Callback called.")
                Toast.makeText(
                    applicationContext,
                    "Ease onCancel Callback called.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            override fun onFinish() {
                Timber.i("Duration onFinish Callback called.")
                Toast.makeText(
                    applicationContext,
                    "Ease onFinish Callback called.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    // # --8<-- [end:callback]

    private lateinit var mapvinaMap: MapVinaMap
    private lateinit var mapView: MapView
    private var cameraState = false
    private val cameraIdleListener = OnCameraIdleListener {
        if (this::mapvinaMap.isInitialized) {
            Timber.w(mapvinaMap.cameraPosition.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_animation_types)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: MapVinaMap) {
        mapvinaMap = map
        mapvinaMap.setStyle(Style.Builder().fromUri(TestStyles.getPredefinedStyleWithFallback("Streets")))
        mapvinaMap.uiSettings.isAttributionEnabled = false
        mapvinaMap.uiSettings.isLogoEnabled = false
        mapvinaMap.addOnCameraIdleListener(cameraIdleListener)

        // handle move button clicks
        val moveButton = findViewById<View>(R.id.cameraMoveButton)
        moveButton?.setOnClickListener { view: View? ->
            // # --8<-- [start:moveCamera]
            val cameraPosition =
                CameraPosition.Builder()
                    .target(nextLatLng)
                    .zoom(14.0)
                    .tilt(30.0)
                    .tilt(0.0)
                    .build()
            mapvinaMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            // # --8<-- [end:moveCamera]
        }

        // handle ease button clicks
        val easeButton = findViewById<View>(R.id.cameraEaseButton)
        easeButton?.setOnClickListener { view: View? ->
            // # --8<-- [start:easeCamera]
            val cameraPosition =
                CameraPosition.Builder()
                    .target(nextLatLng)
                    .zoom(15.0)
                    .bearing(180.0)
                    .tilt(30.0)
                    .build()
            mapvinaMap.easeCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                7500,
                callback
            )
            // # --8<-- [end:easeCamera]
        }

        // handle animate button clicks
        val animateButton = findViewById<View>(R.id.cameraAnimateButton)
        animateButton?.setOnClickListener { view: View? ->
            // # --8<-- [start:animateCamera]
            val cameraPosition =
                CameraPosition.Builder().target(nextLatLng).bearing(270.0).tilt(20.0).build()
            mapvinaMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                7500,
                callback
            )
            // # --8<-- [end:animateCamera]
        }
    }

    private val nextLatLng: LatLng
        get() {
            cameraState = !cameraState
            return if (cameraState) LAT_LNG_TOWER_BRIDGE else LAT_LNG_LONDON_EYE
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
        if (this::mapvinaMap.isInitialized) {
            mapvinaMap.removeOnCameraIdleListener(cameraIdleListener)
        }
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private val LAT_LNG_LONDON_EYE = LatLng(51.50325, -0.11968)
        private val LAT_LNG_TOWER_BRIDGE = LatLng(51.50550, -0.07520)
    }
}
