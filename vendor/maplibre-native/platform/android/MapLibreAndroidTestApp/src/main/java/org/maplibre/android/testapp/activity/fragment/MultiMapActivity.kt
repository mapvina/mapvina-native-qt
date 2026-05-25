package com.mapvina.android.testapp.activity.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.mapvina.android.maps.MapVinaMap
import com.mapvina.android.maps.OnMapReadyCallback
import com.mapvina.android.maps.SupportMapFragment
import com.mapvina.android.testapp.R
import com.mapvina.android.testapp.styles.TestStyles

/**
 * Test Activity showcasing using multiple static map fragments in one layout.
 */
class MultiMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_map)
        val fragmentManager = supportFragmentManager
        initFragmentStyle(fragmentManager, R.id.map1, TestStyles.getPredefinedStyleWithFallback("Streets"))
        initFragmentStyle(fragmentManager, R.id.map2, TestStyles.getPredefinedStyleWithFallback("Bright"))
        initFragmentStyle(fragmentManager, R.id.map3, TestStyles.getPredefinedStyleWithFallback("Satellite Hybrid"))
        initFragmentStyle(fragmentManager, R.id.map4, TestStyles.getPredefinedStyleWithFallback("Pastel"))
    }

    private fun initFragmentStyle(
        fragmentManager: FragmentManager,
        fragmentId: Int,
        styleId: String
    ) {
        (fragmentManager.findFragmentById(fragmentId) as SupportMapFragment?)
            ?.getMapAsync(OnMapReadyCallback { mapvinaMap: MapVinaMap -> mapvinaMap.setStyle(styleId) })
    }
}
