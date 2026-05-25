package com.mapvina.android.testapp.activity.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapvina.android.geometry.LatLngBounds
import com.mapvina.android.location.LocationComponent
import com.mapvina.android.location.LocationComponentActivationOptions
import com.mapvina.android.location.engine.LocationEngine
import com.mapvina.android.location.engine.LocationEngineDefault
import com.mapvina.android.location.engine.LocationEngineRequest
import com.mapvina.android.location.modes.RenderMode
import com.mapvina.android.location.permissions.PermissionsListener
import com.mapvina.android.location.permissions.PermissionsManager
import com.mapvina.android.maps.MapView
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.OnMapReadyCallback
import com.mapvina.android.maps.Style
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

class ManualLocationUpdatesActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private var locationComponent: LocationComponent? = null
    private var locationEngine: LocationEngine? = null
    private var permissionsManager: PermissionsManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_manual_update)
        mapView = findViewById(R.id.mapView)
        locationEngine = LocationEngineDefault.getDefaultLocationEngine(mapView.context)
        val fabManualUpdate = findViewById<FloatingActionButton>(R.id.fabManualLocationChange)
        fabManualUpdate.setOnClickListener { v: View? ->
            if (locationComponent != null && locationComponent!!.locationEngine == null) {
                locationComponent!!.forceLocationUpdate(
                    Utils.getRandomLocation(LatLngBounds.from(60.0, 25.0, 40.0, -5.0))
                )
            }
        }
        fabManualUpdate.isEnabled = false
        val fabToggle = findViewById<FloatingActionButton>(R.id.fabToggleManualLocation)
        fabToggle.setOnClickListener { v: View? ->
            if (locationComponent != null) {
                locationComponent!!.locationEngine =
                    if (locationComponent!!.locationEngine == null) locationEngine else null
                if (locationComponent!!.locationEngine == null) {
                    fabToggle.setImageResource(R.drawable.ic_layers_clear)
                    fabManualUpdate.isEnabled = true
                    fabManualUpdate.alpha = 1f
                    Toast.makeText(
                        this@ManualLocationUpdatesActivity.applicationContext,
                        "LocationEngine disabled, use manual updates",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    fabToggle.setImageResource(R.drawable.ic_layers)
                    fabManualUpdate.isEnabled = false
                    fabManualUpdate.alpha = 0.5f
                    Toast.makeText(
                        this@ManualLocationUpdatesActivity.applicationContext,
                        "LocationEngine enabled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        mapView.onCreate(savedInstanceState)
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            mapView.getMapAsync(this)
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    Toast.makeText(
                        this@ManualLocationUpdatesActivity.applicationContext,
                        "You need to accept location permissions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapView.getMapAsync(this@ManualLocationUpdatesActivity)
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

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapvinaMap: MapVinaMap) {
        mapvinaMap.setStyle(
            Style.Builder().fromUri(TestStyles.getPredefinedStyleWithFallback("Streets"))
        ) { style: Style? ->
            locationComponent = mapvinaMap.locationComponent
            locationComponent!!.activateLocationComponent(
                LocationComponentActivationOptions
                    .builder(this, style!!)
                    .locationEngine(locationEngine)
                    .locationEngineRequest(
                        LocationEngineRequest.Builder(500)
                            .setFastestInterval(500)
                            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY).build()
                    )
                    .build()
            )
            locationComponent!!.isLocationComponentEnabled = true
            locationComponent!!.renderMode = RenderMode.COMPASS
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
