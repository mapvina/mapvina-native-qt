package com.mapvina.android.testapp.activity.location

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.location.LocationComponentActivationOptions
import com.mapvina.android.location.LocationComponentOptions
import com.mapvina.android.location.modes.CameraMode
import com.mapvina.android.location.modes.RenderMode
import com.mapvina.android.location.permissions.PermissionsListener
import com.mapvina.android.location.permissions.PermissionsManager
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.OnMapReadyCallback
import com.mapvina.android.maps.Style
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

class LocationComponentActivationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var mapvinaMap: MapVinaMap
    private var permissionsManager: PermissionsManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_layer_activation_builder)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            mapView.getMapAsync(this)
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    Toast.makeText(
                        this@LocationComponentActivationActivity,
                        "You need to accept location permissions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapView.getMapAsync(this@LocationComponentActivationActivity)
                    } else {
                        finish()
                    }
                }
            })
            permissionsManager!!.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(mapvinaMap: MapVinaMap) {
        this.mapvinaMap = mapvinaMap
        mapvinaMap.setStyle(
            TestStyles.getPredefinedStyleWithFallback("Bright")
        ) { style: Style -> activateLocationComponent(style) }
    }

    @SuppressLint("MissingPermission")
    private fun activateLocationComponent(style: Style) {
        val locationComponent = mapvinaMap.locationComponent
        val locationComponentOptions = LocationComponentOptions.builder(this)
            .elevation(5f)
            .accuracyAlpha(.6f)
            .accuracyColor(Color.GREEN)
            .foregroundDrawable(R.drawable.mapvina_logo_helmet)
            .build()
        val locationComponentActivationOptions = LocationComponentActivationOptions
            .builder(this, style)
            .locationComponentOptions(locationComponentOptions)
            .useDefaultLocationEngine(true)
            .build()
        locationComponent.activateLocationComponent(locationComponentActivationOptions)
        locationComponent.isLocationComponentEnabled = true
        locationComponent.renderMode = RenderMode.NORMAL
        locationComponent.cameraMode = CameraMode.TRACKING
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
