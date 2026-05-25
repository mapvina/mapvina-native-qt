package com.mapvina.android.testapp.activity.textureview

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.mapvina.android.maps.MapVinaMapOptions
import com.mapvina.android.maps.OnMapReadyCallback
import com.mapvina.android.testapp.activity.maplayout.DebugModeActivity
import com.mapvina.android.testapp.utils.NavUtils

/**
 * Test activity showcasing the different debug modes and allows to cycle between the default map styles.
 */
class TextureViewDebugModeActivity : DebugModeActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // activity uses singleInstance for testing purposes
                // code below provides a default navigation when using the app
                NavUtils.navigateHome(this@TextureViewDebugModeActivity)
            }
        })
    }

    override fun setupMapVinaMapOptions(): MapVinaMapOptions {
        val mapvinaMapOptions = super.setupMapVinaMapOptions()
        mapvinaMapOptions.textureMode(true)
        return mapvinaMapOptions
    }
}
