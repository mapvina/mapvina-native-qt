package com.mapvina.android.testapp.activity.storage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mapvina.android.ModuleProvider
import com.mapvina.android.ModuleProviderImpl
import com.mapvina.android.maps.MapView
import com.mapvina.android.MapVina
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.OnMapReadyCallback
import com.mapvina.android.maps.Style
import com.mapvina.android.storage.FileSource
import com.mapvina.android.storage.FileSource.ResourceTransformCallback
import com.mapvina.android.storage.Resource
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.utils.ApiKeyUtils
import com.mapvina.android.testapp.utils.ExampleCustomModuleProviderImpl
import timber.log.Timber

/**
 * This example activity shows how to provide your own HTTP request implementation.
 */
class CustomHttpRequestImplActivity : AppCompatActivity() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_driven_style)

        // Set a custom module provider that provides our custom HTTPRequestImpl
        MapVina.setModuleProvider(ExampleCustomModuleProviderImpl() as ModuleProvider)

        // Initialize map with a style
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(
            OnMapReadyCallback { mapvinaMap: MapVinaMap ->
                mapvinaMap.setStyle(Style.Builder().fromUri("https://maps.mapvina.com/styles/v2/streets.json?key=public_key"))
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Example of how to reset the module provider
        MapVina.setModuleProvider(ModuleProviderImpl())
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
